package org.snp.indexage;

import java.util.Objects;

public class Column {
    private final String name;
    private final String type;
//    private final String tableName;

    private Column(String name, String type) {
        this.name = name;
        this.type= type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name);
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
