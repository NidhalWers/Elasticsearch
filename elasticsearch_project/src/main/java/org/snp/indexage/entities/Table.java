package org.snp.indexage.entities;

import java.util.*;

public class Table {
    private String name;
    private ArrayList<Column> columns;
    private Map<String, Index> indexes = new TreeMap<>();

    private Table(String name, ArrayList<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    private Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addColumn(Column column){
        columns.add(column);
    }
    public void removeColumn(Column column){
        columns.remove(column);
    }
    public ArrayList<Column> getColumns() {
        return columns;
    }

    public boolean addIndex(String key, Index index){
        if(! indexes.containsKey(key)) {
            indexes.put(key, index);
            return true;
        }
        return false;
    }
    public void removeIndex(Index index){ //todo
        indexes.remove(index);
    }

    public ArrayList<Index> getAllIndex(){
        return (ArrayList)(indexes.values());
    }

    public void insertRowIntoIndexes(HashMap<String,String> data, String reference)throws Exception{
        Set<String> keys = indexes.keySet();
        for(String key : keys){
            indexes.get(key).insertLine(data,reference);
        }
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + "\n" +
                stringOfColumns() + "\n" +
                (indexes.keySet().size() > 0 ? "\tindexes :\n"+indexes.keySet() : "" ) +
                '}';
    }

    private String stringOfColumns(){
        String res = "\t columns : \n";
        for(Column column : columns){
            res+= column.toString()+"\n";
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(name, table.name) &&
                Objects.equals(columns, table.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns);
    }







    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String name;
        private ArrayList<Column> columns = new ArrayList<>();

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
