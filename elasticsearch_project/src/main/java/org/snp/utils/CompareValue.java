package org.snp.utils;

import org.snp.model.credentials.AttributeCredentials;

public class CompareValue {

    public String value;

    public AttributeCredentials.Operator operator;

    public CompareValue(String value, AttributeCredentials.Operator operator) {
        this.value = value;
        this.operator = operator;
    }


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public String value;

        public AttributeCredentials.Operator operator;

        public Builder value(String value){
            this.value=value;
            return this;
        }

        public Builder comparison(AttributeCredentials.Operator operator){
            this.operator = operator;
            return this;
        }

        public CompareValue build(){
            return new CompareValue(
                    this.value,
                    this.operator
            );
        }

    }
}
