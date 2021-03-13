package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class IndexCredentials {
    //@JsonProperty("table_name")
    public String tableName;

    public ArrayList<ColumnCredentials> columns;

    public IndexCredentials(){}

    public IndexCredentials(String tableName, ArrayList<ColumnCredentials> columns) {
        this.tableName=tableName;
        this.columns = columns;
    }


}
