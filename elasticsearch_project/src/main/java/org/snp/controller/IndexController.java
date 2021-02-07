package org.snp.controller;


import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.service.IndexService;
import org.snp.utils.exception.NotFoundException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/elasticsearch/index")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IndexController {

    @Inject
    private IndexService indexService;

    @POST
    @Path("/add/")
    public String addIndex(IndexCredentials indexCredentials){
        Message message = indexService.create(indexCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw new NotFoundException("table "+indexCredentials.tableName+" does not exist");
    }
}


