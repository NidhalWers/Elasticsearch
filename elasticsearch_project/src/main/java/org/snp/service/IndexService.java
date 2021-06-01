package org.snp.service;

import org.snp.Main;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.Column;
import org.snp.indexage.Index;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.model.credentials.RemoveIndexCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class IndexService {

    @Inject TableDao tableDao;
    @Inject ColumnService columnService;
    @Inject TableService tableService;
    private SlaveClient[] slaveClients;

    public IndexService(){
        if(Main.isMasterTest())
            slaveClients = new SlaveClient[]{new SlaveClient(8081), new SlaveClient(8082)};
    }

    public Message create(IndexCredentials indexCredentials) {
        Table table = tableDao.find(indexCredentials.tableName);
        if(table == null)
            return new Message(404);

        ArrayList<Column> columns = columnService.getListColumns(indexCredentials.columns);

        if(!tableService.addIndex(table, columns))
            return new Message(403);
        if(Main.isMasterTest()){
            for(SlaveClient slaveClient :slaveClients){
                slaveClient.addIndex(indexCredentials);
            }
        }
        return new MessageAttachment<Table>(200,table);
    }

    public Message delete(String tableName, List<String> columns){
        Table table = tableDao.find(tableName);
        if(table == null){
            return new MessageAttachment<>(404, "table "+tableName+" does not exist");
        }
        Collections.sort(columns);
        String indexKey = String.join(",", columns);
        Index index = table.getIndexes().get(indexKey);
        if(index == null){
            return new MessageAttachment<>(404, "index "+indexKey+ " does not exist in table "+tableName);
        }
        table.getIndexes().remove(indexKey);

        if(Main.isMasterTest()){
            for(SlaveClient slaveClient :slaveClients){
                RemoveIndexCredentials indexCredentials = new RemoveIndexCredentials(tableName, columns);
                slaveClient.deleteIndex(indexCredentials);
            }
        }
        return new MessageAttachment<>(200, " successfully removing index "+indexKey+" in table "+tableName);
    }
}
