package org.snp.indexage.entities;


import org.snp.indexage.helpers.SubIndex;
import org.snp.utils.ListUtils;

import java.util.*;

public class Index {
    private Table table;
    private List<Column> columns;
    //Map of value, position list
    private Map<String, ArrayList<String>> index = new TreeMap<>();

    private Index(Table table, List<Column> columns) {
        this.columns = columns;
    }

    public void insertLine(HashMap<String,String> data, String reference ) throws Exception{
        ArrayList<String> extractedColKey = new ArrayList();
        for(Column column : columns){
            String value = data.get(column.getName()) != null ? data.get(column.getName()) : "NULL" ;
            extractedColKey.add(value);
        }
        Collections.sort(extractedColKey);
        String key = String.join(",",extractedColKey);
        /*if(index.get(key)==null){
            ArrayList newRow = new ArrayList<String>();
            newRow.add(reference);
            index.put(key,newRow);
        }else{
            index.get(key).add(reference);
        }*/
        for(Column columnKey : columns){
            table.getSubIndexMap()
                    .get(columnKey.getName())
                        .insertLine(key, reference);
        }
    }

    public Map<String, ArrayList<String>> getIndex() {
        return index;
    }

    public List<String> find(HashMap<String,String> query){
        List<String> indexValueList = new ArrayList<>();
        /*for(String key : query.keySet()){
            indexValueList.add(query.get(key));
        }
        Collections.sort(indexValueList);
        String indexValue = String.join(",",indexValueList);
        return this.index.get(indexValue);*/

        List<List> allResults = new ArrayList<>();

        for(Map.Entry colum : query.entrySet()  ){
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
