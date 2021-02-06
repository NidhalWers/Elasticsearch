package org.snp.controller;

import org.snp.model.IndexCredentials;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/elasticsearch")
public class Elasticsearch {


    @POST
    @Path("/createtable/")
    public Object createTable(Object obj ){
        return obj;
    }

    @POST
    @Path("/index")
    public Response addIndex(IndexCredentials indexModel){
        return null;
    }
}
