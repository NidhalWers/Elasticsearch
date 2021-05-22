package org.snp.controller;

import org.snp.dao.TableDao;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;
import org.snp.model.credentials.redirection.UpdateAllRefCredentials;
import org.snp.service.TableService;
import org.snp.utils.exception.AlreadyExistException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/table")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableController {

    @Inject
    TableService tableService;
    @Inject
    TableDao tableDAO;

    @POST
    public Table createTable(TableCredentials tableCredentials) {
        if(tableCredentials==null)
            throw new BadRequestException("body can not be empty");
        if(tableCredentials.name==null ||tableCredentials.columns==null){
            throw new BadRequestException("name or columns should not be null");
        }
        Message message =  tableService.create(tableCredentials);
        if(message.hasAttachment())
            return (Table) ((MessageAttachment)message).getAttachment();
        else
            throw new AlreadyExistException("table "+tableCredentials.name+" already exists");
    }
    @GET
    @Path("/all")
    public ArrayList<Table> getAllTable(){
        return tableDAO.findAll();
    }

    @POST
    @Path("/updateRef")
    public void updateAllRefs(UpdateAllRefCredentials updateAllRefCredentials){
        if(updateAllRefCredentials==null)
            throw new BadRequestException("body can not be empty");
        if(updateAllRefCredentials.tableName==null || updateAllRefCredentials.tableName.isBlank())
            throw new BadRequestException("table name can not be blank or empty");
        if(updateAllRefCredentials.difference==0)
            throw new BadRequestException("difference can not be null");

        tableService.updateAllReference(updateAllRefCredentials);
    }

}
