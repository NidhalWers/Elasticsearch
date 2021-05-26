package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;

public class HavingCredentials {
    @JsonbProperty("function")
    public String functionName;
    @JsonbProperty("column")
    public String columnName;

    public AttributeCredentials.Operator operator = AttributeCredentials.Operator.EQ;

    public String value;

    public HavingCredentials() {
    }

}
