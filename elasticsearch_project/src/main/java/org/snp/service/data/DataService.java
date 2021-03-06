package org.snp.service.data;

import org.apache.commons.lang3.StringUtils;
import org.snp.Main;
import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.Dictionnaire;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.AggregateCredentials;
import org.snp.model.credentials.AttributeCredentials;
import org.snp.model.credentials.HavingCredentials;
import org.snp.model.credentials.QueryCredentials;
import org.snp.utils.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class DataService {

    @Inject FileService fileService;
    @Inject FunctionUtils functionUtils;
    @Inject OrderUtils orderUtils;

    final String NODE_NAME = Main.isMasterTest() ? "Master" : System.getProperty("name");
    final String MESSAGE_PREFIX = NODE_NAME + " : ";

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();

    private SlaveClient[] slaveClients;

    public DataService() {
        if (Main.isMasterTest())
            slaveClients = new SlaveClient[]{new SlaveClient(8081), new SlaveClient(8082)};

    }

        /**
         * Select
         *
         */
    public Message query(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ queryCredentials.tableName+" does not exist");

        List<String> references;
        /**
         * column query verification
         */
        if(queryCredentials.queryParams!=null) {
            HashMap<String, CompareValue> queryMap = new HashMap<>();
            for (AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
                String columnName = attributeCredentials.columnName;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, MESSAGE_PREFIX+"column " + columnName + " does not exist in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.columnName, CompareValue.builder()
                                                                        .value(attributeCredentials.value)
                                                                        .comparison(attributeCredentials.operator)
                                                                        .build() );
            }


            references = dataDAO.find(table, queryMap);
            if (!Main.isMasterTest() && (references == null || references.isEmpty()))
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found");
        }else{
            references = dataDAO.findAll(table);
        }

        List<String> linesSelected = new ArrayList<>();

        boolean doNotContinueTest = references == null || references.isEmpty();
        if(! doNotContinueTest) {
            /**
             * all the row
             */
            for (String ref : references) {
                String[] refSplited = ref.split(",");
                linesSelected.add(fileService.getAllDataAtPos(refSplited[0], Integer.valueOf(refSplited[1])));
            }
            /**
             * column selected verification
             */
            if (queryCredentials.columnsSelected != null) {
                List<AggregateCredentials> valideColumnSelected = new ArrayList<>();
                for (AggregateCredentials aggregateCredentials : queryCredentials.columnsSelected) {
                    if (!table.containsColumn(aggregateCredentials.columnName))
                        return new MessageAttachment<>(404, MESSAGE_PREFIX + "can not select : column " + aggregateCredentials.columnName + " does not exist in " + queryCredentials.tableName);
                    if(! functionUtils.isValideFunction(aggregateCredentials.functionName))
                        return new MessageAttachment<>(404, MESSAGE_PREFIX + "can not select : aggregate function named "+ aggregateCredentials.functionName +" does not exist");
                    valideColumnSelected.add(aggregateCredentials);

                }

                linesSelected = getValuesForColumn(table, valideColumnSelected, linesSelected, queryCredentials.queryParams, queryCredentials.having);

            }
        }
        /**
         * redirection to the slaves
         */
        if(Main.isMasterTest()){
            List<String> slaveResult;
            for(SlaveClient slaveClient : slaveClients){
                slaveResult = slaveClient.dataGet(queryCredentials);
                if(slaveResult!=null)
                    linesSelected.addAll(slaveResult);
            }
            if(linesSelected.isEmpty())
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found");
        }
        /**
         * order by
         */
        if(queryCredentials.orderBy != null ){
            for(QueryCredentials.OrderCredentials orderCredentials : queryCredentials.orderBy) {
                if(!table.containsColumn(orderCredentials.columnName)) {
                    return new MessageAttachment<>(404, MESSAGE_PREFIX + "can not order by : column " + orderCredentials.columnName + " does not exist in " + queryCredentials.tableName);
                }
            }
            linesSelected = orderUtils.orderForColumn(table, queryCredentials.columnsSelected, queryCredentials.orderBy, 0, linesSelected);
        }
        /**
         * limit
         * only for the master node
         */
        if(Main.isMasterTest() && queryCredentials.limit != null){
            if(queryCredentials.limit.limit==null || queryCredentials.limit.limit.isBlank())
                return new MessageAttachment<>(401, MESSAGE_PREFIX+" limit value can not be blank");
            int max = linesSelected.size()-1;
            int from = Integer.valueOf(queryCredentials.limit.offset) <= max ? Integer.valueOf(queryCredentials.limit.offset) : max;
            int to = from + Integer.valueOf(queryCredentials.limit.limit) <= max + 1 ? from + Integer.valueOf(queryCredentials.limit.limit) : max +1;
            linesSelected = linesSelected.subList(from,to);
        }
        return new MessageAttachment<List>(200, linesSelected);
    }

    /**
     * DELETE
     *
     */

    public Message delete(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ queryCredentials.tableName+" does not exists");

        List<String> references;
        /**
         * column query verification
         */
        if(queryCredentials.queryParams!=null) {
            HashMap<String, CompareValue> queryMap = new HashMap<>();
            for (AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
                String columnName = attributeCredentials.columnName;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, MESSAGE_PREFIX+"column " + columnName + " does not exists in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.columnName, CompareValue.builder()
                                                                .value(attributeCredentials.value)
                                                                .comparison(attributeCredentials.operator)
                                                                .build());
            }


            references = dataDAO.find(table, queryMap);
            if (!Main.isMasterTest() && (references == null || references.isEmpty()))
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found");
        }else{
            references = dataDAO.findAll(table);
        }

        boolean doNotContinueTest = references == null || references.isEmpty();
        if(! doNotContinueTest) {
            /**
             * suppression in subindex
             */
            for (Dictionnaire dictionnaire : table.getDictionnaireMap().values()) {
                for (String ref : references) {
                    dictionnaire.deleteByReference(ref);
                }
            }
        }

        int finalResult = references.size();
        /**
         * redirection to the slaves
         */
        if(Main.isMasterTest()){
            for(SlaveClient slaveClient : slaveClients){
                finalResult += slaveClient.dataDelete(queryCredentials);
            }
            if(finalResult==0)
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found");

        }

        return new MessageAttachment<>(200, finalResult);
    }

    /**
     * UPDATE
     *
     */
    public Message update(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ queryCredentials.tableName+" does not exists");
        if(queryCredentials.updateParams == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"update_params could not be null");
        if(queryCredentials.queryParams==null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"query_params could not be null");

        List<String> references;
        /**
         * column query verification
         */

        HashMap<String, CompareValue> queryMap = new HashMap<>();
        for (AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
            String columnName = attributeCredentials.columnName;
            if (!table.containsColumn(columnName))
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"column " + columnName + " does not exists in " + queryCredentials.tableName);
            queryMap.put(attributeCredentials.columnName, CompareValue.builder()
                                                            .value(attributeCredentials.value)
                                                            .comparison(attributeCredentials.operator)
                                                            .build());
        }


        references = dataDAO.find(table, queryMap);
        if (!Main.isMasterTest() && (references == null || references.isEmpty()))
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found");

        boolean doNotContinueTest = references == null || references.isEmpty();
        int finalResult=0;
        if(! doNotContinueTest) {
            /**
             * update
             */
            List<String> values = new ArrayList<>();
            for (Map.Entry entry : table.getDictionnaireMap().entrySet()) {
                for (AttributeCredentials attributeCredentials : queryCredentials.updateParams) {
                    String columnName = attributeCredentials.columnName;
                    int columnNumber = table.positionOfColumn(columnName);
                    String newValue = attributeCredentials.value;

                    if (columnName.equals(entry.getKey())) {
                        Dictionnaire dictionnaire = (Dictionnaire) entry.getValue();
                        int size = references.size();
                        finalResult = size;
                        for (int i = 0; i < references.size(); ) {
                            String ref = references.get(i);
                            /**
                             * update in subindex
                             */
                            dictionnaire.updateByReference(newValue, ref);
                            /**
                             * update in file
                             */
                            String[] refSplited = ref.split(",");
                            values.add(fileService.updateColumnAtPos(table, refSplited[0], Integer.valueOf(refSplited[1]), columnNumber, newValue));
                            //cas où on modifie ce qu'on recherche : exemple update nom=Y where nom=X
                            references = dataDAO.find(table, queryMap);
                            if (references == null || references.isEmpty())
                                break;
                            if (references.size() == size)
                                i++;
                        }
                    }
                }
            }
        }

        /**
         * redirection to the slaves
         */

        if(Main.isMasterTest()){
            for(SlaveClient slaveClient : slaveClients){
                finalResult += slaveClient.dataUpdate(queryCredentials);
            }
            if(finalResult==0)
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found");
        }


        return new MessageAttachment<>(200, finalResult);
    }


    /**
     * group By method
     * @param queryCredentials
     * @return
     */

    public Message groupBy(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, MESSAGE_PREFIX+"table "+ queryCredentials.tableName+" does not exist");
        /**
         * group by
         */
        String columnGroupedBy=queryCredentials.groupBy;
        if(! table.containsColumn(columnGroupedBy)){
            return new MessageAttachment<>(404, MESSAGE_PREFIX + "can not group by : column " + columnGroupedBy + " does not exist in " + queryCredentials.tableName);
        }

        QueryCredentials queryForGroupBy = new QueryCredentials(queryCredentials.tableName)
                                            .setColumnSelected()
                                            .addColumn(columnGroupedBy);

        Message messageColumnGroupedByAllValues = query(queryForGroupBy);
        if(messageColumnGroupedByAllValues.getCode() == 200){
            List<String> columnGroupedByALlValues = (List<String>) ((MessageAttachment)messageColumnGroupedByAllValues).getAttachment();

            QueryCredentials queryEachValue;
            List<List<String>> linesSelectedForAllValues = new ArrayList<>();
            Message messageLineForEachValue;

            for(String value : columnGroupedByALlValues){
                /**
                 * query with the original query params & columns selected from
                 * the client request
                 * & adding a query Params on the column we use to group by,
                 * with the value
                 */
                queryEachValue = new QueryCredentials(queryCredentials.tableName)
                                    .setColumnSelected(queryCredentials.columnsSelected)
                                    .setQueryParams(queryCredentials.queryParams)
                                    .addAttribute(columnGroupedBy, value)
                                    .setHaving(queryCredentials.having)
                                    ;


                messageLineForEachValue = query(queryEachValue);
                if(messageLineForEachValue.getCode() == 200){
                    linesSelectedForAllValues.add( (List<String>) ((MessageAttachment)messageLineForEachValue).getAttachment()  );
                }
            }
            if(linesSelectedForAllValues.isEmpty()){
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"data not found during groupBy request");
            }
            /**
             * remove duplicates
             */
            Set<String> setOfLinesSelected = new HashSet<>();
            for(List list : linesSelectedForAllValues){
                setOfLinesSelected.addAll(list);
            }

            List<String> result = new ArrayList<>(setOfLinesSelected);
            /**
             * order by
             */
            if(queryCredentials.orderBy != null ){
                for(QueryCredentials.OrderCredentials orderCredentials : queryCredentials.orderBy) {
                    if(!table.containsColumn(orderCredentials.columnName)) {
                        return new MessageAttachment<>(404, MESSAGE_PREFIX + "can not order by : column " + orderCredentials.columnName + " does not exist in " + queryCredentials.tableName);
                    }
                }
                result = orderUtils.orderForColumn(table, queryCredentials.columnsSelected, queryCredentials.orderBy, 0, result);
            }
            /**
             * only for the master node limit
             */
            if(Main.isMasterTest() && queryCredentials.limit != null){
                if(queryCredentials.limit.limit==null || queryCredentials.limit.limit.isBlank())
                    return new MessageAttachment<>(401, MESSAGE_PREFIX+" limit value can not be blank");
                int from = Integer.valueOf(queryCredentials.limit.offset);
                int to = from + Integer.valueOf(queryCredentials.limit.limit);
                result = result.subList(from,to);
            }
            return new MessageAttachment<List>(200, result);


        }else{
            return messageColumnGroupedByAllValues;
        }
    }



    private List<String> getValuesForColumn(Table table, List<AggregateCredentials> columnsSelected, List<String> completeLines, List<AttributeCredentials> queryParams, List<HavingCredentials> having){
        List<String> result = new ArrayList<>();

        for(String value : completeLines){
            String[] valueSplitted = value.split(",");
            String truncatedLine="";
            boolean havingPassTest = true;
            for(AggregateCredentials column : columnsSelected ){
                int columnPosition = table.positionOfColumn(column.columnName);
                if(column.functionName.equals("None")) {
                    truncatedLine += valueSplitted[columnPosition] + ",";
                }else{
                    Message message = functionUtils.switchFunction(column.functionName, table.getName(), column.columnName, queryParams);
                    if(message.getCode()==200){
                        String attachment = String.valueOf(((MessageAttachment)message).getAttachment());
                        /**
                         * having
                         */
                        if(having!=null && ! having.isEmpty()) {
                            HavingCredentials havingCredentials;
                            if((havingCredentials = HavingUtils.getHavingForFunctionAndColumn(having, column.functionName, column.columnName) ) != null)
                                havingPassTest = havingPassTest && ComparisonUtils.compare(attachment, havingCredentials.value, havingCredentials.operator);
                        }
                        /**
                         * ajoute le résultat à la ligne
                         */
                        truncatedLine += attachment + ",";
                    }
                }
            }
            if(truncatedLine!=null && ! truncatedLine.isBlank()) {
                truncatedLine = StringUtils.chop(truncatedLine);
                if(havingPassTest)
                    result.add(truncatedLine);
            }
        }

        return result;
    }




}
