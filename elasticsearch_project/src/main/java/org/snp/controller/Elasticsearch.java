package org.snp.controller;

import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.credentials.IndexCredentials;
import org.snp.model.credentials.TableCredentials;
import org.snp.utils.FormatUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/elasticsearch")
public class Elasticsearch {
    private Object IndexCredentials;

    //TODO
    //
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

        return table.toString(); //todo adapt the thing that we return
    }




    @POST
    @Path("/createTable/")
    public Table createTable(TableCredentials tableCredentials) {
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
