package org.snp.service;


import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.indexage.entities.SubIndex;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

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
    }


}
