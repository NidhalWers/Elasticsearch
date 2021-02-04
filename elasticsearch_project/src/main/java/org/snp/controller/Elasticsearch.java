package main.java.org.snp.controller;

import main.java.org.snp.dao.model.Column;
import main.java.org.snp.dao.model.Table;
import main.java.org.snp.utils.FormatUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/elasticsearch")
public class Elasticsearch {

    @POST
    @Path("/createtable/")
    @Produces(MediaType.APPLICATION_JSON)
    public Table createTable(@QueryParam("name") String name, @QueryParam("columns") String columns){
        ArrayList<Column> columnArrayListmns = FormatUtils.getListColumns(columns);
        Table table = Table
                .builder()
                .name(name)
                .columns(columnArrayListmns)
                .build();

        return table; //todo adapt the thing that we return
    }
}
