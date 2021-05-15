package org.snp.controller;


import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.service.IndexService;
import org.snp.utils.exception.InternalServerErrorException;
import org.snp.utils.exception.NotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/index")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IndexController {

    @Inject
    IndexService indexService;

    @POST
    @Path("/add") // sert a rien de mettre la route (mais il faudrait enlevé les appelles postman)
    public Table addIndex(IndexCredentials indexCredentials){
        if(indexCredentials==null){
            throw new BadRequestException("body should not be null");
        }try{
            Message message = indexService.create(indexCredentials);
            if(message.hasAttachment())
                return (Table) ((MessageAttachment)message).getAttachment();
            else
                // A revoir - bug quand index existe.
                throw new NotFoundException("table "+indexCredentials.tableName+" does not exist");
        }catch (IOException e){
            e.printStackTrace();
            throw new InternalServerErrorException("IOException");
        }
    }
}


