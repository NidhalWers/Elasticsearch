package org.snp.controller;


import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.service.IndexService;
import org.snp.utils.exception.NotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/index")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IndexController {

    @Inject
    private IndexService indexService;

    @POST
    @Path("/add")
    public Table addIndex(IndexCredentials indexCredentials){
        if(indexCredentials==null){
            throw new BadRequestException("body should not be null");
        }
        Message message = indexService.create(indexCredentials);
        if(message.hasAttachment())
            return (Table) ((MessageAttachment)message).getAttachment();
        else
            throw new NotFoundException("table "+indexCredentials.tableName+" does not exist");
    }
}


