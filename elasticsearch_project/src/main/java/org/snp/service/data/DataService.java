package org.snp.service.data;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.model.credentials.QueryCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class DataService {

    @Inject FileService fileService;

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();


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
                String columnName = attributeCredentials.name;
                if (!table.containsColumn(columnName))
                    return new MessageAttachment<>(404, "column " + columnName + " does not exists in " + queryCredentials.tableName);
                queryMap.put(attributeCredentials.name, attributeCredentials.value);
            }


            references = dataDAO.find(table, queryMap);
            if (references == null || references.isEmpty())
                return new MessageAttachment<>(404, "data not found");
        }else{
            references = dataDAO.findAll(table);
        }

        List<String> values = new ArrayList<>();
        for(String ref : references){
            String[] refSplited = ref.split(",");
            values.add(fileService.getAllDataAtPos(refSplited[0], Integer.valueOf(refSplited[1]), Integer.valueOf(refSplited[2])) );
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
