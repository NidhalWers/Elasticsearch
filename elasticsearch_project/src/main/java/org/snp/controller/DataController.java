package org.snp.controller;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.query.QueryCredentials;
import org.snp.model.credentials.JoinCredentials;
import org.snp.service.data.DataFunctionService;
import org.snp.service.data.DataService;
import org.snp.model.multipart.MultipartBody;
import org.snp.service.data.FileService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class DataController {

    @Inject DataService dataService;
    @Inject FileService fileService;

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/load")
    //link to csv : https://dzone.com/articles/how-to-read-a-big-csv-file-with-java-8-and-stream
    public Table loadData(@MultipartForm MultipartBody data){
        try{
            //save file here
            Message message = fileService.parseCSVAndInsert(data.tableName,data.file,data.fileName);
            if(message.getCode()==200)
                return (Table) ((MessageAttachment)message).getAttachment();
            else{
                if(message.getCode() == 404)
                    throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
                else
                    throw new InternalServerErrorException();
            }
        }catch (IOException e){
            throw new InternalServerErrorException("IOException");
        }
    }


    @POST
    @Path("/query")
    public List<String> get(QueryCredentials queryCredentials){
        if(queryCredentials ==null){
            throw new BadRequestException("query should not be null");
        }
        Message message = dataService.query(queryCredentials);
        if(message.getCode() == 200)
            return (List<String>) ((MessageAttachment)message).getAttachment();
        else {
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new InternalServerErrorException();
        }
    }

    @Inject private
    DataFunctionService dataFunctionService;

    @GET
    @Path("/join")
    public List<String> join(JoinCredentials joinCredentials){
        return null;
    }
}
