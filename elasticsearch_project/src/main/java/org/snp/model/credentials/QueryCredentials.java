package org.snp.model.credentials;


import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;

public class QueryCredentials {
    @JsonbProperty("table_name")
    public String tableName;

    //map key = nom de colonne
    //    value = valeur de la colonne
    @JsonbProperty("query_params")
    public ArrayList<AttributeCredentials> queryParams;

    @JsonbProperty("columns_selected")
    public ArrayList<ColumnCredentials> columnsSelected;

    @JsonbProperty("update_params")
    public  ArrayList<AttributeCredentials> updateParams;

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

        @JsonbProperty("name")
        public String columnName;

        public String value;

        public AttributeCredentials(){}

        public AttributeCredentials(String columnName, String value) {
            this.columnName = columnName;
            this.value = value;
        }
    }
}
