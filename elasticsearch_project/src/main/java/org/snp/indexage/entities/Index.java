package org.snp.indexage.entities;

import java.util.*;

public class Index {
    private List<Column> columns;
    //Map of value, position list
    private Map<String, ArrayList<String>> index = new TreeMap<>();

    private Index(List<Column> columns) {
        this.columns = columns;
    }

    public void insert(String value, String ...keys ){
        String key = "";
        for(String k : keys ){
            key+=k;
        }
        if (index.get(key)==null){
            index.put(key,new ArrayList<String>());
            index.get(key).add(value);
        }else{
            index.get(key).add(value);
        }
    }

    public ArrayList<String> find(String key){
        return index.get(key);
    }





    public static Builder builder(){return new Builder();}

    public static class Builder{
        private List<Column> columns;

        public Builder() {
        }

        public Builder columns(List<Column> columns){
            this.columns=columns;
            return this;
        }

        public Index build(){
            return new Index(this.columns);
        }
    }
}
