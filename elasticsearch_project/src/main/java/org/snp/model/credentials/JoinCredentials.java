package org.snp.model.credentials;


import javax.json.bind.annotation.JsonbProperty;
import java.util.List;

public class JoinCredentials {


    @JsonbProperty("tables")
    public List<JoinTable> tables;

    public JoinCredentials(){}


    public static class JoinTable{
        @JsonbProperty("table_name")
        public String tableName;
        @JsonbProperty("column_name")
        public String columnName;

        public JoinTable(){}
    }
}
