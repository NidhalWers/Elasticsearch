package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

public class FunctionCredentials {

    @JsonbProperty("table_name")
    public String tableName;
    @JsonbProperty("aggregate")
    public AggregateCredentials aggregateCredentials;
    //map key = nom de colonne
    //    value = valeur de la colonne
    @JsonbProperty("query_params")
    public List<AttributeCredentials> queryParams;


    public FunctionCredentials(){}

    public FunctionCredentials(String tableName, AggregateCredentials aggregateCredentials, List<AttributeCredentials> queryParams) {
        this.tableName = tableName;
        this.aggregateCredentials = aggregateCredentials;
        this.queryParams = queryParams;
    }

    public FunctionCredentials setQueryParams(){
        queryParams = new ArrayList<>();
        return this;
    }
    public FunctionCredentials addAttribute(String name, String value){
        queryParams.add( new AttributeCredentials(name, value) );
        return this;
    }


    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        public String tableName;
        public AggregateCredentials aggregateCredentials;
        public List<AttributeCredentials> queryParams;

        public Builder tableName(String tableName){
            this.tableName=tableName;
            return this;
        }
        public Builder aggregate(String function, String column){
            this.aggregateCredentials=new AggregateCredentials(function,column);
            return this;
        }
        public Builder queryParams(List<AttributeCredentials> queryParams){
            this.queryParams=queryParams;
            return this;
        }
        public FunctionCredentials build(){
            return new FunctionCredentials(
                    this.tableName,
                    this.aggregateCredentials,
                    this.queryParams
                    );
        }
    }


}
