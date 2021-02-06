package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnCredentials {
    @JsonProperty("name")
    public String name;
    @JsonProperty("type")
    public String type;

    public ColumnCredentials(){

    }

    public ColumnCredentials(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
