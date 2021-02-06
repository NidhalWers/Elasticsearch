package org.snp.service;

import org.snp.dao.TableDao;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IndexService {

    @Inject private TableDao tableDao;

    public Message create(IndexCredentials indexCredentials){
        Table table = tableDao.find(indexCredentials.tableName);
        if(table == null)
            return new Message(404);


        Index index = new Index(); //todo index constructor
        table.addIndex(index);

        return new MessageAttachment<Table>(200,table);
    }
}
