package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;

@RegisterForReflection
public class TableCredentials {

    @JsonProperty("name")
    private String name;

    @JsonProperty("columns")
    private ArrayList<ColumnCredentials> columns;

    public TableCredentials(){

    }

    public TableCredentials(String name, ArrayList<ColumnCredentials> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ColumnCredentials> getColumns() {
        return columns;
    }
}
