package org.snp.controller;

import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;
import org.snp.model.credentials.JoinCredentials;
import org.snp.service.data.DataFunctionService;
import org.snp.service.data.DataService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController {

    @Inject private DataService dataService;

    @POST
    @Path("/load/")
    public String load(DataCredentials dataCredentials){
        Message message = dataService.load(dataCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw  new NotFoundException("table "+dataCredentials.tableName+" does not exists");
    }


    @POST
    @Path("/get")
    public List<String> get(DataCredentials dataCredentials){
        Message message = dataService.query(dataCredentials);
        if(message.getCode() == 200)
            return (List<String>) ((MessageAttachment)message).getAttachment();
        else {
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new InternalServerErrorException();
        }
    }

    @Inject private
    DataFunctionService dataFunctionService;

    @GET
    @Path("/join")
    public List<String> join(JoinCredentials joinCredentials){
        return null;
    }
}
