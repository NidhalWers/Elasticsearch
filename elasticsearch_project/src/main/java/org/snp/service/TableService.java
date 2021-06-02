package org.snp.service;


import org.snp.Main;
import org.snp.dao.TableDao;
import org.snp.httpclient.SlaveClient;
import org.snp.indexage.Column;
import org.snp.indexage.Index;
import org.snp.indexage.Table;
import org.snp.indexage.Dictionnaire;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;
import org.snp.model.credentials.redirection.UpdateAllRefCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class TableService {
    private static TableDao dao = new TableDao();

    private SlaveClient [] slaveClients;
    private SlaveClient masterClient = new SlaveClient(8080);
    public TableService(){
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

    public Message delete(String tableName){
        Table table = dao.find(tableName);
        if(table == null)
            return new Message(404);

        dao.delete(tableName);
        if(Main.isMasterTest()){
            for(SlaveClient slaveClient :slaveClients){
                slaveClient.deleteTable(tableName);
            }
        }
        return new MessageAttachment<>(200, "Deleting successfully");
    }

    public boolean addIndex(Table table, List<Column> cols){
        Map<String, Dictionnaire> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        for(Column col : cols){
            keys.add(col.getName());
            Dictionnaire dictionnaire = table.getDictionnaireMap().get(col.getName());
            if(dictionnaire == null) {
                dictionnaire = Dictionnaire.builder()
                        .column(col)
                        .build();
                table.getDictionnaireMap().put(col.getName(), dictionnaire);
            }
            map.put(col.getName(), dictionnaire);
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


    public void updateAllReference(Table table, int difference, int from){
        for(Dictionnaire dictionnaire : table.getDictionnaireMap().values()){
            dictionnaire.updateTheReference(difference, from);
        }
        /**
         * redirections
         */
        UpdateAllRefCredentials updateAllRefCredentials = UpdateAllRefCredentials.builder()
                                                            .tableName(table.getName())
                                                            .difference(difference)
                                                            .from(from)
                                                            .build();
        if(Main.isMasterTest()){
            for(SlaveClient slaveClient : slaveClients){
                slaveClient.updateAllRef(updateAllRefCredentials);
            }
        }else{
            for(SlaveClient slaveClient : slaveClients){
                if(! slaveClient.getName().equals(System.getProperty("name"))){
                    slaveClient.updateAllRef(updateAllRefCredentials);
                }
            }
            masterClient.updateAllRef(updateAllRefCredentials);
        }
    }


    private TableDao tableDao = new TableDao();
    public void updateAllReference(UpdateAllRefCredentials updateAllRefCredentials) {
        Table table = tableDao.find(updateAllRefCredentials.tableName);
        if(table!=null) {
            for (Dictionnaire dictionnaire : table.getDictionnaireMap().values()) {
                dictionnaire.updateTheReference(updateAllRefCredentials.difference, updateAllRefCredentials.from);
            }
        }
    }



}
