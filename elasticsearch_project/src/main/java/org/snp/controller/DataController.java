package org.snp.controller;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.DataCredentials;
import org.snp.model.multipart.MultipartBody;
import org.snp.service.DataService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.netty.util.internal.StringUtil.COMMA;

@Path("/data")
public class DataController {

    @Inject private DataService dataService;

    @POST
    @Path("/load/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)

    //link to csv : https://dzone.com/articles/how-to-read-a-big-csv-file-with-java-8-and-stream
    public String loadData(@MultipartForm MultipartBody data){
        List inputList= new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(data.file));

            //inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        }catch (Exception e){

        }
        return null;
    }

    /*
    coordonnée : longitude /lati
    Pair : deux coordonnées
    * */

    @GET
    @Path("/get")
    public String getLigns(DataCredentials dataCredentials){
        Message message = dataService.query(dataCredentials);
        if(message.hasAttachment())
            return ((MessageAttachment)message).getAttachment().toString();
        else
            throw new NotFoundException("table "+dataCredentials.tableName+" does not exists");
    }
}
