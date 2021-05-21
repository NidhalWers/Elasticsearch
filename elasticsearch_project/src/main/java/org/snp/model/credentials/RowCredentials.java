package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;


public class RowCredentials {
    @JsonbProperty
    public String tableName;
    @JsonbProperty
    public String line;
    @JsonbProperty
    public int position;
    @JsonbProperty
    public String fileName;

    public RowCredentials(){}
    public RowCredentials(String tableName, String line, int position, String fileName) {
        this.tableName = tableName;
        this.line = line;
        this.position = position;
        this.fileName = fileName;
    }
}
