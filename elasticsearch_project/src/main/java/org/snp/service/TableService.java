package org.snp.service;


import org.snp.dao.TableDao;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;

public class TableService {
    private static TableDao dao = new TableDao();


    public Message addTable(TableCredentials tableCredentials){
        Table table = dao.find(tableCredentials.getName());
        if(table != null) //already exists
            return new Message(403);

        table = Table
            .builder()
            .name(tableCredentials.getName())
            .build();

        dao.insert(table);
        return new MessageAttachment<Table>(200, table);

    }

}
