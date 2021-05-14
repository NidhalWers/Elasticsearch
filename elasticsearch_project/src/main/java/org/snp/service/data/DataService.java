package org.snp.service.data;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.SubIndex;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.model.credentials.QueryCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DataService {

    @Inject FileService fileService;

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();


    /**
     * Select
     *
     */
    public Message query(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, "table "+ queryCredentials.tableName+" does not exists");

        List<String> references;
        /**
         * column query verification
         */
        if(queryCredentials.queryParams!=null) {
            HashMap<String, String> queryMap = new HashMap<>();
            for (QueryCredentials.AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
                String columnName = attributeCredentials.columnName;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, "column " + columnName + " does not exists in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.columnName, attributeCredentials.value);
            }


            references = dataDAO.find(table, queryMap);
            if (references == null || references.isEmpty())
                return new MessageAttachment<>(404, "data not found");
        }else{
            references = dataDAO.findAll(table);
        }
        /**
         * all the row
         */
        List<String> values = new ArrayList<>();
        for(String ref : references){
            String[] refSplited = ref.split(",");
            values.add(fileService.getAllDataAtPos(refSplited[0], Integer.valueOf(refSplited[1]) ));
        }

        /**
         * column selected verification
         */
        if(queryCredentials.columnsSelected!=null) {
            List<String> columnsName = new ArrayList<>();
            for (ColumnCredentials columnCredentials : queryCredentials.columnsSelected) {
                String columnName = columnCredentials.name;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, "column " + columnName + " does not exists in " + queryCredentials.tableName);
                columnsName.add(columnName);
            }

            values = getValuesForColumn(table, columnsName, values);

        }

         return new MessageAttachment<List>(200, values);
    }

    /**
     * DELETE
     *
     */

    public Message delete(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, "table "+ queryCredentials.tableName+" does not exists");

        List<String> references;
        /**
         * column query verification
         */
        if(queryCredentials.queryParams!=null) {
            HashMap<String, String> queryMap = new HashMap<>();
            for (QueryCredentials.AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
                String columnName = attributeCredentials.columnName;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, "column " + columnName + " does not exists in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.columnName, attributeCredentials.value);
            }


            references = dataDAO.find(table, queryMap);
            if (references == null || references.isEmpty())
                return new MessageAttachment<>(404, "data not found");
        }else{
            references = dataDAO.findAll(table);
        }

        /**
         * suppression in subindex
         */
        for(SubIndex subIndex : table.getSubIndexMap().values()){
            for(String ref : references){
                subIndex.deleteByReference(ref);
            }
        }

        /**
         * suppression in the file
         */



        return new MessageAttachment<>(200, references.size());
    }

    /**
     * UPDATE
     *
     */

    public Message update(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, "table "+ queryCredentials.tableName+" does not exists");
        if(queryCredentials.updateParams == null)
            return new MessageAttachment<>(404, "update_params could not be null");
        if(queryCredentials.queryParams==null)
            return new MessageAttachment<>(404, "query_params could not be null");

        List<String> references;
        /**
         * column query verification
         */

        HashMap<String, String> queryMap = new HashMap<>();
        for (QueryCredentials.AttributeCredentials attributeCredentials : queryCredentials.queryParams) {
            String columnName = attributeCredentials.columnName;
            if (!table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column " + columnName + " does not exists in " + queryCredentials.tableName);
            queryMap.put(attributeCredentials.columnName, attributeCredentials.value);
        }


        references = dataDAO.find(table, queryMap);
        if (references == null || references.isEmpty())
            return new MessageAttachment<>(404, "data not found");


        /**
         * update
         */
        List<String> values = new ArrayList<>();
        for(Map.Entry entry : table.getSubIndexMap().entrySet()){
            for (QueryCredentials.AttributeCredentials attributeCredentials : queryCredentials.updateParams){
                String columnName = attributeCredentials.columnName;
                int columnNumber = table.positionOfColumn(columnName);
                String newValue = attributeCredentials.value;

                if(columnName.equals(entry.getKey())){
                    SubIndex subIndex = (SubIndex) entry.getValue();
                    int size = references.size();
                    for(int i = 0; i<references.size(); ){
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
                        //update the position byte
                        references = dataDAO.find(table, queryMap);
                        if (references == null || references.isEmpty())
                            break;
                        if(references.size() == size)
                            i++;
                    }
                }
            }
        }


        return new MessageAttachment<>(200, references.size());
    }





    private List<String> getValuesForColumn(Table table, List<String> columnsName, List<String> allValues){
        List<String> result = new ArrayList<>();

        for(String value : allValues){
            String[] valueSplitted = value.split(",");
            for(String column : columnsName ){
                int columnPosition = table.positionOfColumn(column);
                result.add(valueSplitted[ columnPosition ]);
            }
        }

        return result;
    }




}
