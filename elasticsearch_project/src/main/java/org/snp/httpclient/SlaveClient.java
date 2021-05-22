package org.snp.httpclient;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import org.snp.model.credentials.*;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(rowCredentials);
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
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(tableCredentials);
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
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(indexCredentials);
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
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(queryCredentials);
        try {
            Response response = post("/data/query",json);
            String responseBody = response.body().string();
            if(response.isSuccessful()) {
                List<String> result = jsonb.fromJson(responseBody, new ArrayList<String>(){}.getClass().getGenericSuperclass());

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
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(queryCredentials);
        try {
            Response response = post("/data/update",json);
            int result = jsonb.fromJson(response.body().string(), Integer.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int dataDelete(QueryCredentials queryCredentials){
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(queryCredentials);
        try {
            Response response = post("/data/delete",json);
            int result = jsonb.fromJson(response.body().string(), Integer.class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
