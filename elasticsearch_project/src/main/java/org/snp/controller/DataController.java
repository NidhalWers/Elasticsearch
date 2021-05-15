package org.snp.controller;

import io.quarkus.security.UnauthorizedException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.snp.Main;
import org.snp.dao.TableDao;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.FunctionCredentials;
import org.snp.model.credentials.QueryCredentials;
import org.snp.model.credentials.JoinCredentials;
import org.snp.model.credentials.RowCredentials;
import org.snp.service.data.FunctionService;
import org.snp.service.data.DataService;
import org.snp.model.multipart.MultipartBody;
import org.snp.service.data.FileService;
import org.snp.utils.exception.AlreadyExistException;

import javax.inject.Inject;
import javax.management.BadAttributeValueExpException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
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
    public Table loadData(@MultipartForm MultipartBody data){
        try{
            //save file here
            if(!Main.isMaster){
                // A RETOURNER 401 NON AUTORISE
                return null;
            }
            if(data.fileName==null || data.fileName.isEmpty() ){
                throw new BadRequestException("Name should not be null");
            }

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
            e.printStackTrace();
            throw new InternalServerErrorException("IOException");
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalServerErrorException("IOException");
        }
    }


    @POST
    @Path("/update")
    public int update(QueryCredentials queryCredentials){
        if(queryCredentials ==null){
            throw new BadRequestException("query should not be null");
        }
            Message message = dataService.update(queryCredentials);
        if(message.getCode() == 200)
            return (int) ((MessageAttachment)message).getAttachment();
        else {
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new InternalServerErrorException();
        }
    }

    @POST
    @Path("/delete")
    public int delete(QueryCredentials queryCredentials){
        if(queryCredentials ==null){
            throw new BadRequestException("query should not be null");
        }
        Message message = dataService.delete(queryCredentials);
        if(message.getCode() == 200)
            return (int) ((MessageAttachment)message).getAttachment();
        else {
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new InternalServerErrorException();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertline")
    public Response insertLine(RowCredentials rowCredentials){
        if (rowCredentials==null || rowCredentials.table.isEmpty() ||rowCredentials.line ==null || rowCredentials.line.isEmpty() ){
            throw new BadRequestException("query should not be null");
        }
        Response response = fileService.insertCsvLineIntoTable(rowCredentials);
        return  response;
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

    @Inject
    FunctionService functionService;

    @POST
    @Path("/join")
    public List<String> join(JoinCredentials joinCredentials){
        if(joinCredentials == null)
            throw new BadRequestException("query should not be null");

        Message message = functionService.join(joinCredentials);
        if(message.getCode() == 200)
            return (List<String>) ((MessageAttachment)message).getAttachment();
        else{
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new InternalServerErrorException();
        }
    }

    @POST
    @Path("/function")
    public double function(FunctionCredentials functionCredentials){
        if(functionCredentials == null)
            throw new BadRequestException("query should not be null");
        if(functionCredentials.functionName == null)
            throw new BadRequestException("function_name should not be null");

        Message message;
        switch (functionCredentials.functionName){
            case "sum" :
                message = functionService.sum(functionCredentials.tableName, functionCredentials.columnName);
                break;
            case "avg" :
                message = functionService.avg(functionCredentials.tableName, functionCredentials.columnName);
                break;
            case "min" :
                message = functionService.min(functionCredentials.tableName, functionCredentials.columnName);
                break;
            case "max" :
                message = functionService.max(functionCredentials.tableName, functionCredentials.columnName);
                break;
            case "count" :
                if( functionCredentials.columnName != null)
                    message = functionService.count(functionCredentials.tableName, functionCredentials.columnName);
                else
                    message = functionService.count(functionCredentials.tableName);
                break;
            default:
                throw new BadRequestException("function_name does not correspond");
        }

        if(message.getCode() == 200){
            return (double) ((MessageAttachment)message).getAttachment();
        }else{
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new BadRequestException((String) ((MessageAttachment)message).getAttachment());
        }
    }
}
