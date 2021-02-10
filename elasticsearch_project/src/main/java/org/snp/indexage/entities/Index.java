package org.snp.indexage.entities;


import org.snp.indexage.helpers.SubIndex;
import org.snp.utils.ListUtils;

import java.util.*;

public class Index {
    private Table table;
    private List<Column> columns;

    private Index(Table table, List<Column> columns) {
        this.table=table; this.columns = columns;
    }

    public void insertLine(HashMap<String,String> data, String reference ) throws Exception{
        ArrayList<String> extractedColKey = new ArrayList();
        for(Column column : columns){
            String value = data.get(column.getName()) != null ? data.get(column.getName()) : "NULL" ;
            extractedColKey.add(value);
        }
        Collections.sort(extractedColKey);
        String key = String.join(",",extractedColKey);
        for(Column columnKey : columns){
            table.getSubIndexMap()
                    .get(columnKey.getName())
                        .insertLine(key, reference);
        }
    }

    public List<String> find(HashMap<String,String> query){
        List<List> allResults = new ArrayList<>();

        for(Map.Entry colum : query.entrySet()){
            allResults.add(table.getSubIndexMap()
                                    .get(colum.getKey())
                                        .find((String) colum.getValue()));
        }
        return ListUtils.intersection(allResults);
    }

    public boolean containsColumn(String colName){
        for(Column col : columns){
            if(col.getName()==colName){
                return true;
            }
        }
        return false;
    }
    public static Builder builder(){return new Builder();}

    public static class Builder{
        private List<Column> columns;
        private Table table;

        public Builder() {
        }

        public Builder columns(List<Column> columns){
            this.columns=columns;
            return this;
        }

        public Builder table(Table table){
            this.table=table;
            return this;
        }

        public Index build(){
            return new Index(this.table, this.columns);
        }
    }
}
