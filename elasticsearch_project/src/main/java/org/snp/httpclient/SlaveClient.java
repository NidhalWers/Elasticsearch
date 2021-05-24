package org.snp.httpclient;

import com.squareup.okhttp.Response;

import org.snp.Main;
import org.snp.model.credentials.*;
import org.snp.model.credentials.redirection.RowCredentials;
import org.snp.model.credentials.redirection.UpdateAllRefCredentials;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlaveClient extends HttpClient{

    private final String name;

    public SlaveClient(int port) {
        super(port);
        name = Main.isMasterTest() ? "Master" : System.getProperty("name");
    }

    public String getName() {
        return name;
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
            post("/data/insertline",json);
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
            String body = response.body().string();
            if(response.isSuccessful()) {
                List<String> result = jsonb.fromJson(body, new ArrayList<String>(){}.getClass().getGenericSuperclass());

                return result;
            }
            return null;
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
            String body = response.body().string();
            if(response.isSuccessful()) {
                int result = jsonb.fromJson(body, Integer.class);
                return result;
            }
            System.out.println(body);
            return 0;
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
            String body = response.body().string();
            if(response.isSuccessful()) {
                int result = jsonb.fromJson(body, Integer.class);
                return result;
            }
            System.out.println(body);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateAllRef(UpdateAllRefCredentials updateAllRefCredentials){
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(updateAllRefCredentials);
        try {
            post("/table/updateRef",json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
