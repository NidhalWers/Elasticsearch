package org.snp.httpclient;

import com.squareup.okhttp.*;

import java.io.IOException;

public class HttpClient {

    private final int port;
    private static final String BASE_URI = "http://localhost:";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public HttpClient(int port) {
        this.port = port;
    }

    public Response get(String uri) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URI + port + uri)
                .build();
        return client.newCall(request).execute();
    }

    public Response post(String uri, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URI + port + uri)
                .post(requestBody)
                .build();
        return client.newCall(request).execute();
    }



}
