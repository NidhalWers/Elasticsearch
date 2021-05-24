package org.snp.service.data;

import org.apache.commons.lang3.StringUtils;
import org.snp.Main;
import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.SubIndex;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.AggregateCredentials;
import org.snp.model.credentials.AttributeCredentials;
import org.snp.model.credentials.QueryCredentials;
import org.snp.utils.FunctionUtils;
import org.snp.utils.OrderUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            HashMap<String, String> queryMap = new HashMap<>();
            for (AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
                String columnName = attributeCredentials.columnName;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, MESSAGE_PREFIX+"column " + columnName + " does not exist in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.columnName, attributeCredentials.value);
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

                linesSelected = getValuesForColumn(table, valideColumnSelected, linesSelected, queryCredentials.queryParams);

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
         * group by
         */
        if(queryCredentials.groupBy != null && ! queryCredentials.groupBy.isEmpty()){
            for(String columnName : queryCredentials.groupBy){
                if(! table.containsColumn(columnName)){
                    return new MessageAttachment<>(404, MESSAGE_PREFIX + "can not group by : column " + columnName + " does not exist in " + queryCredentials.tableName);
                }
            }

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
            HashMap<String, String> queryMap = new HashMap<>();
            for (AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
                String columnName = attributeCredentials.columnName;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, MESSAGE_PREFIX+"column " + columnName + " does not exists in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.columnName, attributeCredentials.value);
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
            for (SubIndex subIndex : table.getSubIndexMap().values()) {
                for (String ref : references) {
                    subIndex.deleteByReference(ref);
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

        HashMap<String, String> queryMap = new HashMap<>();
        for (AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
            String columnName = attributeCredentials.columnName;
            if (!table.containsColumn(columnName))
                return new MessageAttachment<>(404, MESSAGE_PREFIX+"column " + columnName + " does not exists in " + queryCredentials.tableName);
            queryMap.put(attributeCredentials.columnName, attributeCredentials.value);
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
            for (Map.Entry entry : table.getSubIndexMap().entrySet()) {
                for (AttributeCredentials attributeCredentials : queryCredentials.updateParams) {
                    String columnName = attributeCredentials.columnName;
                    int columnNumber = table.positionOfColumn(columnName);
                    String newValue = attributeCredentials.value;

                    if (columnName.equals(entry.getKey())) {
                        SubIndex subIndex = (SubIndex) entry.getValue();
                        int size = references.size();
                        finalResult = size;
                        for (int i = 0; i < references.size(); ) {
                            String ref = references.get(i);
                            /**
                             * update in subindex
                             */
                            subIndex.updateByReference(newValue, ref);
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





    private List<String> getValuesForColumn(Table table, List<AggregateCredentials> columnsSelected, List<String> completeLines, List<AttributeCredentials> queryParams){
        List<String> result = new ArrayList<>();

        for(String value : completeLines){
            String[] valueSplitted = value.split(",");
            String truncatedLine="";
            for(AggregateCredentials column : columnsSelected ){
                int columnPosition = table.positionOfColumn(column.columnName);
                if(column.functionName.equals("None")) {
                    truncatedLine += valueSplitted[columnPosition] + ",";
                }else{
                    Message message = functionUtils.switchFunction(column.functionName, table.getName(), column.columnName, queryParams);
                    if(message.getCode()==200){
                        truncatedLine +=String.valueOf(((MessageAttachment)message).getAttachment()) + ",";
                    }
                }
            }
            if(truncatedLine!=null && ! truncatedLine.isBlank()) {
                truncatedLine = StringUtils.chop(truncatedLine);
                result.add(truncatedLine);
            }
        }

        return result;
    }




}
