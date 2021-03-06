package org.snp.model.credentials;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JoinCredentials {


    @JsonProperty("tables")
    public List<JoinTable> tables;

    public JoinCredentials(){}


    public class JoinTable{
        @JsonProperty("table_name")
        public String tableName;
        @JsonProperty("column_name")
        public String columnName;

        public JoinTable(){}
    }
}
