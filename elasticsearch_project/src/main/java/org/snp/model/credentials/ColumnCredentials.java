package org.snp.model.credentials;


import javax.json.bind.annotation.JsonbProperty;

public class ColumnCredentials {
    @JsonbProperty("name")
    public String columnName;
    public String type;

    public ColumnCredentials(){

    }

    public ColumnCredentials(String columnName, String type) {
        this.columnName = columnName;
        this.type = type;
    }

    public ColumnCredentials(String columnName) {
        this.columnName = columnName;
    }
}
