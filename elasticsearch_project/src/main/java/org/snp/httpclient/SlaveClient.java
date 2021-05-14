package org.snp.httpclient;

import com.squareup.okhttp.ResponseBody;
import org.snp.model.credentials.RowCredentials;
import org.snp.model.response.RowInsertedModel;

import java.io.IOException;

public class SlaveClient extends HttpClient{

    public SlaveClient(int port) {
        super(port);
    }

    public RowInsertedModel insertLine(RowCredentials rowCredentials) throws IOException {
        String json = gson.toJson(rowCredentials);
        ResponseBody responseBody = post("/data/",json);
        RowInsertedModel rowInsertedModel= gson.fromJson(responseBody.string(),RowInsertedModel.class);
        return rowInsertedModel;
    }


}
