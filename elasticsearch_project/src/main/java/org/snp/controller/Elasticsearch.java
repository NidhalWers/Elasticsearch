package org.snp.controller;

import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.IndexCredentials;
import org.snp.model.credentials.TableCredentials;
import org.snp.service.TableService;
import org.snp.utils.FormatUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/elasticsearch")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Elasticsearch {

    @POST
    @Path("/createtablewithparam/")
    @Produces(MediaType.APPLICATION_JSON)
    public String createTable(@QueryParam("name") String name, @QueryParam("columns") String columns){
        ArrayList<Column> columnArrayListmns = FormatUtils.getListColumns(columns);
        Table table = Table
                .builder()
                .name(name)
                .columns(columnArrayListmns)
                .build();

        return table.toString();
    }


    @Inject
    private TableService tableService;

    @POST
    @Path("/createTable/")
    public Table createTable(TableCredentials tableCredentials) {
        Message message =  tableService.create(tableCredentials);
        if(message.hasAttachment()){
            return (Table)((MessageAttachment)message).getAttachment();
        }
        return null;
    }



    @POST
    @Path("/addIndex/")
    public Response addIndex(IndexCredentials indexCredentials){
        return null;
    }




    @POST
    @Path("/loadData/")
    public Response loadData(){
        return null;
    }




    @GET
    @Path("/getIndexLigns")
    public Response getIndexLigns(IndexCredentials indexCredentials){
        return null;
    }




}
