package org.snp.service;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;

import java.util.List;

public class DataService {

    private static TableDao tableDao = new TableDao();
    private static DataDao dataDAO = new DataDao();

    public Message load(DataCredentials dataCredentials){
        Table table = tableDao.find(dataCredentials.tableName);
        if(table == null)
            return new Message(404);

        try {
            dataDAO.insertRowIntoIndexes(table, dataCredentials.data, null); //todo reference
            return new MessageAttachment<Table>(200, table);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(500);
        }
    }

    public Message query(DataCredentials dataCredentials){
        Table table = tableDao.find(dataCredentials.tableName);
        if(table == null)
            return new Message(404);

        List<String> values = dataDAO.executeQuery(table, dataCredentials.data);
        if(values == null)
            return new Message(404);

        return new MessageAttachment<List>(200, values);
    }


}
