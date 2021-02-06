package org.snp.service;

import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IndexService {

    @Inject private TableService tableService;

    public Message create(IndexCredentials indexCredentials){
        Message message = tableService.get(indexCredentials.tableName);
        if(! message.hasAttachment())
            return new Message(404);

        Table table = (Table)((MessageAttachment)message).getAttachment();

        Index index = new Index(); //todo index constructor
        table.addIndex(index);

        return new MessageAttachment<Table>(200,table);
    }
}
