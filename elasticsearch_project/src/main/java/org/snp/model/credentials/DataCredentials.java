package org.snp.model.credentials;

import java.util.HashMap;

public class DataCredentials {
    public String tableName;

    //map key = nom de colonne
    //    value = valeur de la colonne
    public HashMap<String, String> data;

    public DataCredentials() {
    }

    public DataCredentials(String tableName, HashMap<String, String> data) {
        this.tableName = tableName;
        this.data = data;
    }

}
