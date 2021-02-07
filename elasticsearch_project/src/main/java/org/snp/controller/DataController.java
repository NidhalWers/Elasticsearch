package org.snp.controller;

import org.snp.model.credentials.IndexCredentials;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/elasticsearch/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController {


    @POST
    @Path("/load/")
    public String loadData(){
        return null;
    }




    @GET
    @Path("/get/")
    public String getLigns(){
        return null;
    }
}
