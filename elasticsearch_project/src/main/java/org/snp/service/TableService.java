package org.snp.service;


import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;

@ApplicationScoped
public class TableService {
    private static TableDao dao = new TableDao();

    @Inject private ColumnService columnService;

    public Message create(TableCredentials tableCredentials){
        Table table = dao.find(tableCredentials.name);
        if(table != null) //already exists
            return new Message(403);

        ArrayList<Column> columns = columnService.getListColumns(tableCredentials.columns);

        table = Table
            .builder()
            .name(tableCredentials.name)
            .columns(columns)
            .build();
        dao.insert(table);
        return new MessageAttachment<Table>(200, table);
    }

    public Message get(String key){
        Table table = dao.find(key);
        if(table == null)
            return new Message(404);

        return new MessageAttachment<Table>(200, table);
    }

}
