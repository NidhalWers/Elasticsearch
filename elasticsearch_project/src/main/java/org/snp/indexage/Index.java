package org.snp.indexage;


import org.snp.utils.CompareValue;
import org.snp.utils.ListUtils;

import java.util.*;

public class Index {
    private List<Column> columns;
    private Map<String, SubIndex> subIndexMap;

    private Index(List<Column> columns, Map<String, SubIndex> subIndexMap) {
        this.columns = columns;
        this.subIndexMap=subIndexMap;
    }

    public void insertLine(HashMap<String,String> data, String reference ){
        for(Column column : columns){
            String value = data.getOrDefault(column.getName(),"NULL")  ;
            subIndexMap
                    .get(column.getName())
                        .insertLine(value, reference);
        }
    }

    public List<String> find(HashMap<String, CompareValue> query){
        List<List> allResults = new ArrayList<>();
        for(Map.Entry<String, CompareValue> colum : query.entrySet()){
            List subIndexMapList = subIndexMap
                    .get(colum.getKey())
                    .find(colum.getValue());
            if(subIndexMapList!=null){
                allResults.add(subIndexMapList);
            }
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

    @Override
    public String toString() {
        return "Index{" +
                "columns=" + columns +
                ", subIndexMap=" + subIndexMap +
                '}';
    }

    /**
     * Builder
     *
     */

    public static Builder builder(){return new Builder();}

    public static class Builder{
        private List<Column> columns;
        private Map<String, SubIndex> subIndexMap;

        public Builder() {
        }

        public Builder columns(List<Column> columns){
            this.columns=columns;
            return this;
        }


        public Builder subIndexes(Map<String, SubIndex> subIndexMap){
            this.subIndexMap = subIndexMap;
            return this;
        }

        public Index build(){
            return new Index(this.columns, this.subIndexMap);
        }
    }

    public List<Column> getColumns() {
        return columns;
    }
}
