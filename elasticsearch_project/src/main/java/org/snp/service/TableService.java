package org.snp.service;


import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;
import org.snp.model.credentials.TableCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class TableService {
    private static TableDao dao = new TableDao();
    @Inject
    ColumnService columnService;
    public Message create(TableCredentials tableCredentials){
        Table table = dao.find(tableCredentials.getName());
        if(table != null) //already exists
            return new Message(403);

        ArrayList<Column> columns = columnService.getListColumns(tableCredentials.getColumns());

        table = Table
            .builder()
            .name(tableCredentials.getName())
            .columns(columns)
            .build();
        dao.insert(table);
        return new MessageAttachment<Table>(200, table);

    }

    public Message addLine(DataCredentials dataCredentials){
        Table table = dao.find(dataCredentials.tableName);
        if(table == null)
            return new Message(404);

        try {
            table.insertRowIntoIndexes(dataCredentials.data, null);
            return new MessageAttachment<Table>(200, table);
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(500);
        }
    }

    public Message query(DataCredentials dataCredentials){
        Table table = dao.find(dataCredentials.tableName);
        if(table == null)
            return new Message(404);

        List<String> values = table.executeQuery(dataCredentials.data);
        if(values == null)
            return new Message(404);

        return new MessageAttachment<List>(200, values);
    }

}
