package main.java.org.snp.dao.model;

public class Column {
    private String name;
    private String type;
    //todo use an enum for the type, and convert the string to his enum value

    private Column(String name, String type) {
        this.name = name;
        this.type = type;
    }


    public static Builder builder(){return new Builder();}

    public static class Builder{
        private String name;
        private String type;

        public Builder(){
        }

        public Builder name(String name){
            this.name=name;
            return this;
        }

        public Builder type(String type){
            this.type=type;
            return this;
        }

        public Column build(){
            return new Column(this.name, this.type);
        }
    }
}
