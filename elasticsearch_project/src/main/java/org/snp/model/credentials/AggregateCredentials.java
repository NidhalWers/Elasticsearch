package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;

public class AggregateCredentials {
    @JsonbProperty("function_name")
    public String functionName;
    @JsonbProperty("column_name")
    public String columnName;

    public AggregateCredentials() {
    }
}
