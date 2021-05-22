package org.snp.service;


import org.snp.Main;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.Column;
import org.snp.indexage.Index;
import org.snp.indexage.Table;
import org.snp.indexage.SubIndex;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class TableService {
    private static TableDao dao = new TableDao();
    private SlaveClient [] slaveClients;

    public TableService(){
        if(Main.isMasterTest())
            slaveClients = new SlaveClient[]{new SlaveClient(8081), new SlaveClient(8082)};
    }

    @Inject
    ColumnService columnService;
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
        if(Main.isMasterTest()){
            for(SlaveClient slaveClient :slaveClients){
                slaveClient.createTable(tableCredentials);
            }
        }
        return new MessageAttachment<Table>(200, table);
    }

    public boolean addIndex(Table table, List<Column> cols){
        Map<String, SubIndex> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        for(Column col : cols){
            keys.add(col.getName());
            SubIndex subIndex = table.getSubIndexMap().get(col.getName());
            if(subIndex == null) {
                subIndex = SubIndex.builder()
                        .column(col)
                        .build();
                table.getSubIndexMap().put(col.getName(), subIndex);
            }
            map.put(col.getName(), subIndex);
        }
        Index newIndex = Index.builder()
                .columns(cols)
                .subIndexes(map)
                .build();
        Collections.sort(keys);
        String indexKey = String.join(",", keys);
        if(table.getIndexes().get(indexKey)!=null){
            return false;
        }else {
            table.getIndexes().put(indexKey,newIndex);
        }
        return true;
    }

    public void removeIndex(Table table, Index index){ //todo
        table.getIndexes().remove(index);
    }
    //todo remove index with a list of column's name and test


    public void updateAllReference(Table table, int difference, int from){
        for(SubIndex subIndex : table.getSubIndexMap().values()){
            subIndex.updateTheReference(difference, from);
        }
        //todo redirection to all the other nodes
    }


}
