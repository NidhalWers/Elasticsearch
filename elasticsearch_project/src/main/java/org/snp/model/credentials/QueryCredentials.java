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


    public static class AttributeCredentials {

        @JsonProperty("name")
        public String name;

        public String value;

        public AttributeCredentials(){}

    }
}
