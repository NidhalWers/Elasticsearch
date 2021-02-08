package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class TableCredentials {

    @JsonProperty("name")
    public String name;

    @JsonProperty("columns")
    public ArrayList<ColumnCredentials> columns;

    public TableCredentials(){

    }

    public String getName() {
        return name;
    }

    public ArrayList<ColumnCredentials> getColumns() {
        return columns;
    }

    public TableCredentials(String name, ArrayList<ColumnCredentials> columns) {
        this.name = name;
        this.columns = columns;
    }
}
