package org.snp.httpclient;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import org.snp.model.credentials.IndexCredentials;
import org.snp.model.credentials.RowCredentials;
import org.snp.model.credentials.TableCredentials;
import org.snp.model.response.RowInsertedModel;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;

public class SlaveClient extends HttpClient{

    public SlaveClient(int port) {
        super(port);
    }

    public RowInsertedModel insertLine(RowCredentials rowCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(rowCredentials);
        try {
            Response response = post("/data/insertline",json);

            if(!response.isSuccessful()){
                throw  new InternalServerErrorException("error during the redirection of insertLine");
            }
            RowInsertedModel rowInsertedModel= null;
            rowInsertedModel = gson.fromJson(response.body().string(), RowInsertedModel.class);
            return rowInsertedModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void createTable(TableCredentials tableCredentials) {
        Gson gson = new Gson();
        String json = gson.toJson(tableCredentials);
        try {
            Response response = post("/table", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addIndex(IndexCredentials indexCredentials){
        Gson gson = new Gson();
        String json = gson.toJson(indexCredentials);
        try {
            Response response = post("/index/add", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
