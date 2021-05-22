package org.snp.model.credentials.redirection;

public class UpdateAllRefCredentials {

    public String tableName;

    public int difference;

    public int from;

    public UpdateAllRefCredentials() {
    }

    private UpdateAllRefCredentials(String tableName, int difference, int from) {
        this.tableName = tableName;
        this.difference = difference;
        this.from = from;
    }

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        public String tableName;

        public int difference;

        public int from;

        public Builder tableName(String tableName){
            this.tableName=tableName;
            return this;
        }
        public Builder difference(int difference){
            this.difference=difference;
            return this;
        }
        public Builder from(int from){
            this.from=from;
            return this;
        }
        public UpdateAllRefCredentials build(){
            return new UpdateAllRefCredentials(
                    tableName,
                    difference,
                    from
            );
        }
    }
}
