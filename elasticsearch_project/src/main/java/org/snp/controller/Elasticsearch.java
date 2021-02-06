package org.snp.controller;

import org.snp.model.Column;
import org.snp.model.Table;
import org.snp.utils.FormatUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

//TODO
// this class will include all our endpoints

@Path("/elasticsearch")
public class Elasticsearch {

    //TODO
    // this method is used as an example at the moment, to understand endpoints with quarkus, as well as tests.
    // We will change to POST later

    @POST
    @Path("/createtable/")
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
}
