package org.snp.controller;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;
import org.snp.model.credentials.JoinCredentials;
import org.snp.service.data.DataFunctionService;
import org.snp.service.data.DataService;
import org.snp.model.multipart.MultipartBody;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class DataController {

    @Inject private DataService dataService;
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    @POST
    //link to csv : https://dzone.com/articles/how-to-read-a-big-csv-file-with-java-8-and-stream
    public Table loadData(@MultipartForm MultipartBody data){
        try{
            //save file here
            Message message = dataService.parseCSVAndInsert(data.tableName,data.file,data.fileName);
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
    public List<String> get(DataCredentials dataCredentials){
        if(dataCredentials==null){
            throw new BadRequestException("query should not be null");
        }
        Message message = dataService.query(dataCredentials);
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
