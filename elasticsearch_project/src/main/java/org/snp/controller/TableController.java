package org.snp.controller;

import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;
import org.snp.service.TableService;
import org.snp.utils.exception.AlreadyExistException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/table")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableController {

    @Inject
    TableService tableService;

    @POST
    @Path("/")
    public Table createTable(TableCredentials tableCredentials) {
        if(tableCredentials.name==null ||tableCredentials.columns==null){
            throw new BadRequestException("name or columns should not be null");
        }
        Message message =  tableService.create(tableCredentials);
        if(message.hasAttachment())
            return (Table) ((MessageAttachment)message).getAttachment();
        else
            throw new AlreadyExistException("table "+tableCredentials.name+" already exists");
    }

}
