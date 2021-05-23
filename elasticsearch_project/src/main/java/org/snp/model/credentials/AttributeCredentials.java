package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;

public class AttributeCredentials {

    @JsonbProperty("name")
    public String columnName;

    public String value;

    public AttributeCredentials(){}

    public AttributeCredentials(String columnName, String value) {
        this.columnName = columnName;
        this.value = value;
    }
}
