package org.snp.service.data;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.query.AttributeCredentials;
import org.snp.model.credentials.query.QueryCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
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

        HashMap<String, String> queryMap = new HashMap<>();
        for(AttributeCredentials attributeCredentials : queryCredentials.queryParams){
            String columnName = attributeCredentials.name;
            if(! table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column "+columnName+" does not exists in "+ queryCredentials.tableName);
            queryMap.put(attributeCredentials.name, attributeCredentials.value);
        }

        List<String> references = dataDAO.find(table, queryMap);
        if(references == null || references.isEmpty())
            return new MessageAttachment<>(404, "data not found");

        List<String> values = new ArrayList<>();
        for(String ref : references){
            String[] refSplited = ref.split(",");
            values.add(fileService.getAllDataAtPos(refSplited[0], Integer.valueOf(refSplited[1]), Integer.valueOf(refSplited[2])) );
        }

        return new MessageAttachment<List>(200, values);
    }

    public Message parseCSVAndInsert(String tableName, InputStream csvFile, String fileName) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(csvFile);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        String[] values;
        List<Column> columns;
        Table table = tableDao.find(tableName);
        int position=0;
        if(table==null){
            return new MessageAttachment<>(404, "table "+tableName+" does not exists");
        }
        HashMap<String, String> lineToInsert;
        try {
            columns =table.getColumns();
            line = bufferedReader.readLine();
            position+=line.getBytes().length+1;
            while ((line = bufferedReader.readLine())!= null) {
                values = line.split(",");
                lineToInsert = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    lineToInsert.put(columns.get(i).getName(), values[i]);
                }
                int  lineLength = line.getBytes().length;
                dataDAO.insert(table, lineToInsert,fileName+","+(position+1)+","+lineLength);
                position+=lineLength+1;
            }
        } catch (Exception e) {
            System.err.println(e);
            return new Message(500);
        } finally {
            bufferedReader.close();
            inputStreamReader.close();
        }

        return new MessageAttachment<Table>(200, table);
    }




    }
