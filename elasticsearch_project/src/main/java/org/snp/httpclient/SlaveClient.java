package org.snp.httpclient;

import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.snp.model.credentials.RowCredentials;
import org.snp.model.response.RowInsertedModel;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;

public class SlaveClient extends HttpClient{

    public SlaveClient(int port) {
        super(port);
    }

    public RowInsertedModel insertLine(RowCredentials rowCredentials) throws Exception {
        String json = gson.toJson(rowCredentials);
        Response response = post("/data/insertline",json);
        if(!response.isSuccessful()){
            throw  new InternalServerErrorException();
        }
        RowInsertedModel rowInsertedModel= gson.fromJson(response.body().string(),RowInsertedModel.class);
        return rowInsertedModel;
    }


}
