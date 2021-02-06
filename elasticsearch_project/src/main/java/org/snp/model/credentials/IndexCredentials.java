package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IndexCredentials {
    @JsonProperty("tableName")
    public String tableName;
    @JsonProperty("index")
    public Map<String, ArrayList<String>> index = new TreeMap<>();
    @JsonProperty("columns")
    public List<ColumnCredentials> columns = new ArrayList<>();

    public IndexCredentials(){}

    public IndexCredentials(String tableName, Map<String, ArrayList<String>> index, List<ColumnCredentials> columns) {
        this.tableName=tableName;
        this.index = index;
        this.columns = columns;
    }


}
