package org.snp.utils;

import org.snp.model.credentials.AttributeCredentials;

public class CompareValue {

    public String value;

    public AttributeCredentials.Comparison comparison;

    public CompareValue(String value, AttributeCredentials.Comparison comparison) {
        this.value = value;
        this.comparison = comparison;
    }


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public String value;

        public AttributeCredentials.Comparison comparison;

        public Builder value(String value){
            this.value=value;
            return this;
        }

        public Builder comparison(AttributeCredentials.Comparison comparison){
            this.comparison=comparison;
            return this;
        }

        public CompareValue build(){
            return new CompareValue(
                    this.value,
                    this.comparison
            );
        }

    }
}
