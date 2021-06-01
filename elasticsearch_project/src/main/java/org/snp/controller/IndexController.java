package org.snp.controller;


import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.model.credentials.RemoveIndexCredentials;
import org.snp.service.IndexService;
import org.snp.utils.exception.NotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.file.AccessDeniedException;

@Path("/index")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IndexController {

    @Inject
    IndexService indexService;

    @POST
    @Path("/add")
    public Table addIndex(IndexCredentials indexCredentials) throws AccessDeniedException {
        if(indexCredentials==null)
            throw new BadRequestException("body should not be null");
        if(indexCredentials.tableName==null || indexCredentials.tableName.isBlank())
            throw new BadRequestException("table name can not be blank or empty");
        if(indexCredentials.columns==null || indexCredentials.columns.isEmpty())
            throw new BadRequestException("impossible to create an index without column");

        Message message = indexService.create(indexCredentials);

        if(message.hasAttachment())
            return (Table) ((MessageAttachment)message).getAttachment();
        else {
            if (message.getCode() == 404)
                throw new NotFoundException("table " + indexCredentials.tableName + " does not exist");
            else
                throw new AccessDeniedException("this index already exist in " + indexCredentials.tableName + " table");
        }
    }

    @DELETE
    @Path("/remove")
    public String removeIndex(RemoveIndexCredentials indexCredentials){
        if(indexCredentials == null)
            throw new BadRequestException("body should not be null");
        if(indexCredentials.tableName==null || indexCredentials.tableName.isBlank())
            throw new BadRequestException("table name can not be blank or empty");
        if(indexCredentials.columns==null || indexCredentials.columns.isEmpty())
            throw new BadRequestException("impossible to create an index without column");

        Message message = indexService.delete(indexCredentials.tableName, indexCredentials.columns);

        if(message.getCode() == 200){
            return (String) ((MessageAttachment)message).getAttachment();
        }else{
            throw new NotFoundException( (String) ((MessageAttachment)message).getAttachment() );
        }
    }
}


