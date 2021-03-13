package org.snp.model.credentials.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeCredentials {

    @JsonProperty("name")
    public String name;

    public String value;

    public AttributeCredentials(){}

}
