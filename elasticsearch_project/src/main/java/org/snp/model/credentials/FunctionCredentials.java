package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

public class FunctionCredentials {

    @JsonbProperty("function_name")
    public String functionName;
    @JsonbProperty("table_name")
    public String tableName;
    @JsonbProperty("column_name")
    public String columnName;
    //map key = nom de colonne
    //    value = valeur de la colonne
    @JsonbProperty("query_params")
    public List<AttributeCredentials> queryParams;


    public FunctionCredentials(){}


    public FunctionCredentials setQueryParams(){
        queryParams = new ArrayList<>();
        return this;
    }
    public FunctionCredentials addAttribute(String name, String value){
        queryParams.add( new AttributeCredentials(name, value) );
        return this;
    }

}
