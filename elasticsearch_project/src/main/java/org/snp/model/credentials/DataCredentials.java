package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class DataCredentials {
    //@JsonProperty("table_name")
    public String tableName;

    //map key = nom de colonne
    //    value = valeur de la colonne
    //@JsonProperty("query_params")
    public HashMap<String, String> queryParams;

    public DataCredentials() {
    }

    public DataCredentials(String tableName, HashMap<String, String> queryParams) {
        this.tableName = tableName;
        this.queryParams = queryParams;
    }

}
