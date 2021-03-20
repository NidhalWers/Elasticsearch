package org.snp.model.credentials;



import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;

public class IndexCredentials {
    @JsonbProperty("table_name")
    public String tableName;

    public ArrayList<ColumnCredentials> columns;

    public IndexCredentials(){}

    public IndexCredentials(String tableName, ArrayList<ColumnCredentials> columns) {
        this.tableName=tableName;
        this.columns = columns;
    }


}
