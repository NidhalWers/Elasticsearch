package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;
import java.util.List;

public class RemoveIndexCredentials {
    @JsonbProperty("table_name")
    public String tableName;

    public List<String> columns;

    public RemoveIndexCredentials(){}

    public RemoveIndexCredentials(String tableName, List<String> columns) {
        this.tableName=tableName;
        this.columns = columns;
    }


}

