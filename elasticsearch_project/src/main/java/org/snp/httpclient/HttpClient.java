package org.snp.httpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.*;
import org.snp.model.credentials.RowCredentials;
import org.snp.model.response.RowInsertedModel;

import java.io.IOException;

public class HttpClient {

    private final int port;
    private static final String BASE_URI = "http://localhost";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    public HttpClient(int port) {
        this.port = port;
    }

    public Response get(String uri) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URI+port+uri)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response;
    }

    public Response post(String uri, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://localhost:" + port + uri)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response;
    }



}
