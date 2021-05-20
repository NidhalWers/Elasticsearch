package org.snp.httpclient;

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
        String json = gson.toJson(rowCredentials);
        Response response = null;
        try {
            response = post("/data/insertline",json);

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
        String json = gson.toJson(tableCredentials);
        try {
            Response response = post("/table", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addIndex(IndexCredentials indexCredentials){
        String json = gson.toJson(indexCredentials);
        try {
            Response response = post("/index/add", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
