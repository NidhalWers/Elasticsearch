package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;
import java.util.HashMap;


public class RowCredentials {
    @JsonbProperty
    public String table;
    @JsonbProperty
    public String line;
    @JsonbProperty
    public int position;
    @JsonbProperty
    public String fileName;

    public RowCredentials(String table,String line) {
        this.table = table;
        this.line = line;
    }
}
