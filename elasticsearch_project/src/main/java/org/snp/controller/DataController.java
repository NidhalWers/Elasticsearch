package org.snp.controller;

import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;
import org.snp.service.data.DataService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController {

    @Inject private DataService dataService;

    @POST
    @Path("/load/")
    public String loadData(DataCredentials dataCredentials){
        Message message = dataService.load(dataCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw  new NotFoundException("table "+dataCredentials.tableName+" does not exists");
    }

    /*
    coordonnée : longitude /lati
    Pair : deux coordonnées
    * */


    @GET
    @Path("/get")
    public String getLigns(DataCredentials dataCredentials){
        Message message = dataService.query(dataCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw new NotFoundException("table "+dataCredentials.tableName+" does not exists");
    }
}
