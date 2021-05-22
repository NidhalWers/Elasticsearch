package org.snp.httpclient;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import org.snp.model.credentials.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlaveClient extends HttpClient{

    public SlaveClient(int port) {
        super(port);
    }

    /**
     * Redirection to insert data
     * @param rowCredentials
     * @return
     */
    public void insertLine(RowCredentials rowCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(rowCredentials);
        try {
            Response response = post("/data/insertline",json);
            /*
            if(!response.isSuccessful()){
                throw  new InternalServerErrorException("error during the redirection of insertLine");
            }
            RowInsertedModel rowInsertedModel= null;
            rowInsertedModel = gson.fromJson(response.body().string(), RowInsertedModel.class);
            return rowInsertedModel;
            */
        } catch (IOException e) {
            e.printStackTrace();
            //return null;
        }

    }

    /**
     * Redirection to create Table
     * @param tableCredentials
     */
    public void createTable(TableCredentials tableCredentials) {
        Gson gson = new Gson();
        String json = gson.toJson(tableCredentials);
        try {
            post("/table", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * redirection to add index
     * @param indexCredentials
     */
    public void addIndex(IndexCredentials indexCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(indexCredentials);
        try {
            post("/index/add", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * redirection to query data
     */

    public List<String> dataGet(QueryCredentials queryCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(queryCredentials);//todo à revoir
        try {
            Response response = post("/data/query",json);
            System.out.println("\nresponse message : "+response.body().string());
            if(response.isSuccessful()) {
                List<String> result = gson.fromJson(response.body().string(), List.class);
                return result;
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int dataUpdate(QueryCredentials queryCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(queryCredentials);
        try {
            Response response = post("/data/update",json);
            int result = gson.fromJson(response.body().string(), Integer.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int dataDelete(QueryCredentials queryCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(queryCredentials);
        try {
            Response response = post("/data/delete",json);
            int result = gson.fromJson(response.body().string(), Integer.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
