package main.java.org.snp.dao.model;

import java.util.ArrayList;

public class Table {
    private String name;
    private ArrayList<Column> columns;

    private Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    private Table(String name) {
        this.name = name;
    }

    public void addColumn(Column column){
        columns.add(column);
    }

    public void removeColumn(Column column){
        columns.remove(column);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String name;
        private ArrayList<Column> columns;

        public Builder(){

        }

        public Builder name(String name){
            this.name=name;
            return this;
        }

        public Builder columns(ArrayList<Column> columns){
            this.columns=columns;
            return this;
        }

        public Table build(){
            return new Table(name,columns);
        }
    }



}
