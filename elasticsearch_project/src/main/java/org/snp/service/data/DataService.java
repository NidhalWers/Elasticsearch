package org.snp.service.data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        for(Map.Entry m : dataCredentials.data.entrySet()){
            String columnName = (String) m.getKey();
            if(! table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column "+columnName+" does not exists in "+dataCredentials.tableName);
        }
        try {
            dataDAO.insert(table, dataCredentials.data, null); //todo reference
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

        for(Map.Entry m : dataCredentials.data.entrySet()){
            String columnName = (String) m.getKey();
            if(! table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column "+columnName+" does not exists in "+dataCredentials.tableName);
        }

        List<String> values = dataDAO.find(table, dataCredentials.data);
        if(values == null)
            return new MessageAttachment<>(404, "data not found");

        return new MessageAttachment<List>(200, values);
    }

    public boolean parseCSVAndInsert(String tableName, InputStream csvFile) throws IOException {
        CSVReader csvReader = new CSVReader(new InputStreamReader(csvFile));
        String[] line;
        String[] colNames;
        HashMap<String, String> lineToInsert;
        int lineCounter = 0;
        try {
            colNames = csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                lineToInsert = new HashMap<>();
                for (int i = 0; i < line.length; i++) {
                    lineToInsert.put(colNames[i], line[i]);
                }
                dataDAO.insert(tableDao.find(tableName), lineToInsert, lineCounter + "");
                lineCounter++;
            }

        } catch (CsvValidationException e) {
            System.err.println(e);
            return false;
        } finally {
            csvReader.close();
        }
        return true;
    }




    }
