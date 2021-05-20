package org.snp.service;

import org.snp.Main;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.Column;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.model.credentials.IndexCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

@ApplicationScoped
public class IndexService {

    @Inject TableDao tableDao;
    @Inject ColumnService columnService;
    @Inject TableService tableService;
    private SlaveClient[] slaveClients = {new SlaveClient(8081),new SlaveClient(8082)};

    public Message create(IndexCredentials indexCredentials) {
        Table table = tableDao.find(indexCredentials.tableName);
        if(table == null)
            return new Message(404);

        String[] columnsName = new String[indexCredentials.columns.size()];
        int i=0;
        for(ColumnCredentials c : indexCredentials.columns){
            columnsName[i] = c.name;
            i++;
        }

        ArrayList<Column> columns = columnService.getListColumns(indexCredentials.columns);

        if(!tableService.addIndex(table, columns))
            return new Message(403);
        if(Main.isMaster){
            for(SlaveClient slaveClient :slaveClients){
                slaveClient.addIndex(indexCredentials);
            }
        }
        return new MessageAttachment<Table>(200,table);
    }
}
