package org.snp.model.credentials;

import javax.json.bind.annotation.JsonbProperty;

public class AggregateCredentials {
    @JsonbProperty("function")
    public String functionName;
    @JsonbProperty("column")
    public String columnName;

    public AggregateCredentials() {
    }

    public AggregateCredentials(String functionName) {
        this.functionName = functionName;
    }
}
