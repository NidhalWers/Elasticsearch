package org.snp.controller;

import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.model.credentials.TableCredentials;
import org.snp.service.IndexService;
import org.snp.service.TableService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/elasticsearch")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Elasticsearch {

    private static final String MY_URL = "http://localhost:8080/elasticsearch/";

    @Inject
    private TableService tableService;

    @POST
    @Path("/createTable/")
    public String createTable(TableCredentials tableCredentials) {
        Message message =  tableService.create(tableCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();

        return "table "+tableCredentials.name+" already exists";
    }


    @Inject
    private IndexService indexService;

    @POST
    @Path("/addIndex/")
    public String addIndex(IndexCredentials indexCredentials){
        Message message = indexService.create(indexCredentials);
        if(message.hasAttachment())
                return ((MessageAttachment)message).getAttachment().toString();

        return "table "+indexCredentials.tableName+" not found";
    }




    @POST
    @Path("/loadData/")
    public String loadData(){
        return null;
    }




    @GET
    @Path("/getIndexLigns")
    public String getIndexLigns(IndexCredentials indexCredentials){
        return null;
    }




}
