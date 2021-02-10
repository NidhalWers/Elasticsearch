package org.snp.service;

import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.model.credentials.IndexCredentials;
import org.snp.utils.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;

@ApplicationScoped
public class IndexService {

    @Inject private TableDao tableDao;
    @Inject private ColumnService columnService;

    public Message create(IndexCredentials indexCredentials){
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

        if(! table.createIndex(columns))
            return new Message(403);

        return new MessageAttachment<Table>(200,table);
    }
}
