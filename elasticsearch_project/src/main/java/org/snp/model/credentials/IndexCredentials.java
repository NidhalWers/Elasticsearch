package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IndexCredentials {
    public String tableName;
    @JsonProperty("columns")
    public ArrayList<ColumnCredentials> columns;

    public IndexCredentials(){}

    public IndexCredentials(String tableName, ArrayList<ColumnCredentials> columns) {
        this.tableName=tableName;
        this.columns = columns;
    }


}
