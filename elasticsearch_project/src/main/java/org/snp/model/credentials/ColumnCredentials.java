package org.snp.model.credentials;

public class ColumnCredentials {
    private String name;
    private String type;

    public ColumnCredentials(){

    }

    public ColumnCredentials(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
