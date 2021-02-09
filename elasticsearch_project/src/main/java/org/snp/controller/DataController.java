package org.snp.controller;

import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;
import org.snp.model.credentials.IndexCredentials;
import org.snp.service.TableService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/elasticsearch/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController {

    @Inject private TableService tableService;

    @POST
    @Path("/load/")
    public String loadData(DataCredentials dataCredentials){
        Message message = tableService.addLign(dataCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw  new NotFoundException("table "+dataCredentials.tableName+" does not exists");
    }




    @GET
    @Path("/get/")
    public String getLigns(DataCredentials dataCredentials){
        Message message = tableService.query(dataCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw new NotFoundException("table "+dataCredentials.tableName+" does not exists");
    }
}
