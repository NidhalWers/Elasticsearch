package org.snp.indexage;

import java.util.*;



public class Table {

    private String name;
    private List<Column> columns;
    private Map<String, Index> indexes = new TreeMap<>();
    private Map<String, Dictionnaire> dictionnaireMap = new HashMap<>();

    private Table(String name, List<Column> columns) {
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
        //todo remove sub index
        // remove with a name
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Map<String, Index> getIndexes() {
        return indexes;
    }

    public Map<String, Dictionnaire> getDictionnaireMap() {
        return dictionnaireMap;
    }


    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + "\n" +
                stringOfColumns() + "\n" +
                (indexes.keySet().size() > 0 ? "\tindexes :\n"+indexes.keySet() : "" ) +
                (dictionnaireMap.keySet().size() > 0 ? "\tsub indexes : \n"+ dictionnaireMap.keySet(): "")+
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

    public boolean containsColumn(String columnName){
        for(Column column : columns){
            if( column.getName().equals(columnName) )
                return true;
        }
        return false;
    }

    public int positionOfColumn(String columnName){
        for(int i =0; i<columns.size(); i++){
            if(columns.get(i).getName().equals(columnName))
                return i;
        }
        return -1;
    }

    public Column getColumnFromName(String name){
        for(Column column : columns){
            if( column.getName().equals(name) )
                return column;
        }
        return null;
    }

    public String getValueForColumn(String columnName, String value){
        String[] valueSplitted = value.split(",");
        int columnPosition = positionOfColumn(columnName);
        return valueSplitted[ columnPosition ];
    }


    /**
     * Builder
     *
     */

    /**
     * Builder
     *
     */

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String name;
        private List<Column> columns = new ArrayList<>();

        public Builder(){

        }

        public Builder name(String name){
            this.name=name;
            return this;
        }

        public Builder columns(List<Column> columns){
            this.columns=columns;
            return this;
        }

        public Table build(){
            return new Table(name,columns);
        }
    }



}
