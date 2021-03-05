package org.snp.indexage.entities;

import org.snp.indexage.helpers.SubIndex;

import java.util.*;



public class Table {

    private String name;
    private List<Column> columns;
    private Map<String, Index> indexes = new TreeMap<>();
    private Map<String, SubIndex> subIndexMap = new HashMap<>();

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
        //todo remove with a name
    }

    public List<Column> getColumns() {
        return columns;
    }

    public boolean createIndex(List<Column> cols){
        Map<String, SubIndex> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        for(Column col : cols){
            keys.add(col.getName());
            SubIndex subIndex = subIndexMap.get(col.getName());
            if(subIndex == null) {
                subIndex = SubIndex.builder()
                                    .column(col)
                                    .build();
                subIndexMap.put(col.getName(), subIndex);
            }
            map.put(col.getName(), subIndex);
        }

        Index newIndex = Index.builder()
                        .columns(cols)
                        .subIndexes(map)
                        .build();

        Collections.sort(keys);
        String indexKey = String.join(",", keys);
        if(indexes.get(indexKey)!=null){
            return false;
        }else {
            indexes.put(indexKey,newIndex);
        }
        return true;
    }
    public void removeIndex(Index index){ //todo
        indexes.remove(index);
    }
    //todo remove index with a list of column's name

    public Map<String, Index> getIndexes() {
        return indexes;
    }

    public Map<String, SubIndex> getSubIndexMap() {
        return subIndexMap;
    }


    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + "\n" +
                stringOfColumns() + "\n" +
                (indexes.keySet().size() > 0 ? "\tindexes :\n"+indexes.keySet() : "" ) +
                (subIndexMap.keySet().size() > 0 ? "\tsub indexes : \n"+subIndexMap.keySet(): "")+
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
