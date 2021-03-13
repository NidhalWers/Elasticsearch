package org.snp.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class QueryCredentials {
    //@JsonProperty("table_name")
    public String tableName;

    //map key = nom de colonne
    //    value = valeur de la colonne
    //@JsonProperty("query_params")
    public ArrayList<AttributeCredentials> queryParams;

    public ArrayList<ColumnCredentials> columnsSelected;

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
    public QueryCredentials setColumnSelected(){
        columnsSelected = new ArrayList<>();
        return this;
    }
    public QueryCredentials addColumn(String name){
        columnsSelected.add(new ColumnCredentials(name));
        return this;
    }

    public static class AttributeCredentials {

        @JsonProperty("name")
        public String name;

        public String value;

        public AttributeCredentials(){}

        public AttributeCredentials(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
