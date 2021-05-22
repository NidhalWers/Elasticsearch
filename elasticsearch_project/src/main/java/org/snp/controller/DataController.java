package org.snp.controller;

import io.quarkus.security.UnauthorizedException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.snp.Main;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.FunctionCredentials;
import org.snp.model.credentials.QueryCredentials;
import org.snp.model.credentials.JoinCredentials;
import org.snp.model.credentials.RowCredentials;
import org.snp.service.data.FunctionService;
import org.snp.service.data.DataService;
import org.snp.model.credentials.DataCredentials;
import org.snp.service.data.FileService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController {

    private static final String FILE_PREFIX = "elasticsearch_";

    @Inject DataService dataService;
    @Inject FileService fileService;

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/load")
    //link to csv : https://dzone.com/articles/how-to-read-a-big-csv-file-with-java-8-and-stream
    public Table loadData(@MultipartForm DataCredentials data){
        try{
            if(!Main.isMasterTest()){
                throw new NotAuthorizedException("not authorized to load data in a slave node");
            }
            Message message = fileService.parseCSVAndInsert(data.tableName,data.file,(FILE_PREFIX+data.tableName));
            if(message.getCode()==200)
                return (Table) ((MessageAttachment)message).getAttachment();
            else{
                if(message.getCode() == 404)
                    throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
                else
                    throw new InternalServerErrorException("internal error");
            }
        }catch (IOException e){
            throw new InternalServerErrorException("IOException");
        }
    }

    @POST
    @Path("/insertline")
    public Table insertLine(RowCredentials rowCredentials){
        if(Main.isMasterTest())
            throw new UnauthorizedException("not authorized to invoke this method in master node");
        if (rowCredentials==null || rowCredentials.tableName.isBlank() || rowCredentials.line ==null || rowCredentials.line.isBlank() ){
            throw new BadRequestException("query should not be null, blank or empty");
        }
        System.out.println("\ninsert line : " + rowCredentials.line + " in "+System.getProperty("name"));
        Message message = fileService.insertCsvLineIntoTable(rowCredentials);
        if(message.getCode()==200)
            return (Table) ((MessageAttachment)message).getAttachment();
        else{
            if(message.getCode() == 404)
                throw new NotFoundException((String) ((MessageAttachment)message).getAttachment());
            else
                throw new InternalServerErrorException("internal error");
        }
    }



    @POST
    @Path("/query")
    public List<String> get(QueryCredentials queryCredentials){
        if(queryCredentials ==null){
            throw new BadRequestException("query should not be null");
        }
        System.out.println("\n\ttable name "+queryCredentials.tableName);
        System.out.println("\n\tquery "+queryCredentials.queryParams);
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
