package org.snp.model.credentials;


import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

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
    public List<AggregateCredentials> columnsSelected;

    @JsonbProperty("update_params")
    public List<AttributeCredentials> updateParams;

    @JsonbProperty("group_by")
    public String groupBy;

    @JsonbProperty("order_by")
    public List<OrderCredentials> orderBy;

    @JsonbProperty("having")
    public List<HavingCredentials> having;

    public LimitCredentials limit;

    public QueryCredentials() {
    }

    public QueryCredentials(String tableName) {
        this.tableName = tableName;
    }

    /**
     *set query params
     */
    public QueryCredentials setQueryParams(){
        queryParams = new ArrayList<>();
        return this;
    }
    public QueryCredentials addAttribute(String name, String value){
        queryParams.add( new AttributeCredentials(name, value) );
        return this;
    }
    public QueryCredentials setQueryParams(List<AttributeCredentials> queryParams){
        this.queryParams = new ArrayList<>();
        if(queryParams!=null)
            this.queryParams.addAll(queryParams);
        return this;
    }

    /**
     *set column selected
     */

    public QueryCredentials setColumnSelected(){
        columnsSelected = new ArrayList<>();
        return this;
    }
    public QueryCredentials setColumnSelected(List<AggregateCredentials> columnsSelected){
        this.columnsSelected = new ArrayList<>();
        if(columnsSelected!=null)
            this.columnsSelected.addAll(columnsSelected);
        return this;
    }
    public QueryCredentials addColumn(String name) {
        columnsSelected.add(new AggregateCredentials(name));
        return this;
    }

    /**
     * having
     */
    public QueryCredentials setHaving(List<HavingCredentials> having){
        this.having = new ArrayList<>();
        if(having != null)
            this.having.addAll(having);
        return this;
    }

    public static class OrderCredentials{
        @JsonbProperty("column_name")
        public String columnName;

        public OrderDirection order = OrderDirection.ASC;
    }

    public enum OrderDirection{

        @JsonEnumDefaultValue
        ASC("asc"),

        DESC("desc")

        ;

        private String value;

        OrderDirection() {
        }

        OrderDirection(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
