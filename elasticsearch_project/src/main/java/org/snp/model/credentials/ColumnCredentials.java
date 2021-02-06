package org.snp.model.credentials;

public class ColumnCredentials {
    public String name;
    public String type;

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
