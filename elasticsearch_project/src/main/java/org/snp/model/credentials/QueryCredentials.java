package org.snp.model.credentials;


import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

public class QueryCredentials {
    @JsonbProperty("table_name")
    public String tableName;

    //map key = nom de colonne
    //    value = valeur de la colonne
    @JsonbProperty("query_params")
    public List<AttributeCredentials> queryParams;

    @JsonbProperty("columns_selected")
    public List<ColumnCredentials> columnsSelected;

    @JsonbProperty("update_params")
    public List<AttributeCredentials> updateParams;

    public QueryCredentials() {
    }

    public QueryCredentials(String tableName) {
        this.tableName = tableName;
    }

    public QueryCredentials setQueryParams(){
        queryParams = new ArrayList<>();
        return this;
    }
    public QueryCredentials addAttribute(String name, String value){
        queryParams.add( new AttributeCredentials(name, value) );
        return this;
    }
    public QueryCredentials setQueryParams(List<AttributeCredentials> queryParams){
        this.queryParams=queryParams;
        return this;
    }

    public QueryCredentials setColumnSelected(){
        columnsSelected = new ArrayList<>();
        return this;
    }
    public QueryCredentials addColumn(String name){
        columnsSelected.add(new ColumnCredentials(name));
        return this;
    }


}
