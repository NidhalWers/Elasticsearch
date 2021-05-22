package org.snp.model.credentials.redirection;

import javax.json.bind.annotation.JsonbProperty;


public class RowCredentials {
    @JsonbProperty
    public String tableName;
    @JsonbProperty
    public String line;
    @JsonbProperty
    public int position;
    @JsonbProperty
    public String fileName;

    public RowCredentials(){}
    private RowCredentials(String tableName, String line, int position, String fileName) {
        this.tableName = tableName;
        this.line = line;
        this.position = position;
        this.fileName = fileName;
    }

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        public String tableName;
        public String line;
        public int position;
        public String fileName;

        public Builder tableName(String tableName){
            this.tableName=tableName;
            return this;
        }
        public Builder line(String line){
            this.line=line;
            return this;
        }
        public Builder position(int position){
            this.position=position;
            return this;
        }
        public Builder fileName(String fileName){
            this.fileName=fileName;
            return this;
        }

        public RowCredentials build(){
            return new RowCredentials(
                    tableName,
                    line,
                    position,
                    fileName
            );
        }

    }


}
