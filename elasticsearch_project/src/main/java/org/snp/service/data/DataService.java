package org.snp.service.data;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DataService {

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();

    public Message load(DataCredentials dataCredentials){
        Table table = tableDao.find(dataCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, "table "+dataCredentials.tableName+" does not exists");

        for(Map.Entry m : dataCredentials.queryParams.entrySet()){
            String columnName = (String) m.getKey();
            if(! table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column "+columnName+" does not exists in "+dataCredentials.tableName);
        }
        try {
            dataDAO.insert(table, dataCredentials.queryParams, null); //todo reference
            return new MessageAttachment<Table>(200, table);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(500);
        }
    }

    public Message query(DataCredentials dataCredentials){
        Table table = tableDao.find(dataCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, "table "+dataCredentials.tableName+" does not exists");

        for(Map.Entry m : dataCredentials.queryParams.entrySet()){
            String columnName = (String) m.getKey();
            if(! table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column "+columnName+" does not exists in "+dataCredentials.tableName);
        }

        List<String> values = dataDAO.find(table, dataCredentials.queryParams);
        if(values == null)
            return new MessageAttachment<>(404, "data not found");

        return new MessageAttachment<List>(200, values);
    }

    public boolean parseCSVAndInsert(String tableName, InputStream csvFile, String fileName) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(csvFile);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        String[] values;
        List<Column> columns;
        Table table = tableDao.find(tableName);
        int position=0;
        if(table==null){
            return false;
        }
        HashMap<String, String> lineToInsert;
        try {
            columns =table.getColumns();
            line = bufferedReader.readLine();
            position+=line.getBytes().length;
            while ((line = bufferedReader.readLine())!= null) {
                values = line.split(",");
                lineToInsert = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                    lineToInsert.put(columns.get(i).getName(), values[i]);
                }
                int  lineLength = line.getBytes().length;
                dataDAO.insert(table, lineToInsert,fileName+","+position+","+lineLength);
                position+=lineLength;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        } finally {
            bufferedReader.close();
            inputStreamReader.close();
        }
        return true;
    }




    }
