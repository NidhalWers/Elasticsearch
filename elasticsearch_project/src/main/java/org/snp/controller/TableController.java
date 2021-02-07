package org.snp.controller;

import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.TableCredentials;
import org.snp.service.TableService;
import org.snp.utils.exception.AlreadyExistException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/elasticsearch/table")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TableController {


    @Inject
    private TableService tableService;

    @POST
    @Path("/create/")
    public String createTable(TableCredentials tableCredentials) {
        Message message =  tableService.create(tableCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw new AlreadyExistException("table "+tableCredentials.name+" already exists");
    }

}
