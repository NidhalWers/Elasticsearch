package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;

public class FunctionCredentials {

    @JsonbProperty("function_name")
    public String functionName;
    @JsonbProperty("table_name")
    public String tableName;
    @JsonbProperty("column_name")
    public String columnName;

    public FunctionCredentials(){}
}
