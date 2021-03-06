package org.snp.model.credentials;


import com.fasterxml.jackson.annotation.JsonValue;

import javax.json.bind.annotation.JsonbProperty;

public class AttributeCredentials {

    @JsonbProperty("name")
    public String columnName;

    public String value;

    public Operator operator = Operator.EQ;

    public AttributeCredentials(){}

    public AttributeCredentials(String columnName, String value) {
        this.columnName = columnName;
        this.value = value;;
    }

    public enum Operator {

        EQ("="),

        SUP_OR_EQ(">="),

        SUP(">"),

        INF_OR_EQ("<="),

        INF("<"),

        DIF("!=")

        ;


        private final String value;

        Operator(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

    }
}
