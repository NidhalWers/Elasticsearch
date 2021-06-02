package org.snp.httpclient;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    private final int port;
    private static final String BASE_URI = "http://localhost:";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public HttpClient(int port) {
        this.port = port;
    }

    public Response get(String uri) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.MINUTES);
        client.setReadTimeout(15, TimeUnit.MINUTES);
        client.setWriteTimeout(15, TimeUnit.MINUTES);
        Request request = new Request.Builder()
                .url(BASE_URI + port + uri)
                .build();
        return client.newCall(request).execute();
    }

    public Response post(String uri, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.MINUTES);
        client.setReadTimeout(15, TimeUnit.MINUTES);
        client.setWriteTimeout(15, TimeUnit.MINUTES);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URI + port + uri)
                .post(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    public Response delete(String uri, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.MINUTES);
        client.setReadTimeout(15, TimeUnit.MINUTES);
        client.setWriteTimeout(15, TimeUnit.MINUTES);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URI + port + uri)
                .delete(requestBody)
                .build();
        return client.newCall(request).execute();
    }



}
