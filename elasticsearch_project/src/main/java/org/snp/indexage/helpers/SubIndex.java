package org.snp.indexage.helpers;

import org.snp.indexage.entities.Column;

import java.util.*;

public class SubIndex {
    private Column column;

    private Map<String, List<String>> referenceMap = new LinkedHashMap<>();

    private SubIndex(Column column) {
        this.column = column;
    }

    public void insertLine(String key, String reference){
        if(referenceMap.get(key)==null){
            List refList = new ArrayList();
            refList.add(reference);
            referenceMap.put(key, refList);
        }else
            referenceMap.get(key).add(reference);
    }

    public Column getColumn() {
        return column;
    }

    public List<String> find(String key){
        return referenceMap.get(key);
    }


    @Override
    public String toString() {
        return "SubIndex{" +
                "column=" + column +
                ", referenceMap=" + referenceMap +
                '}';
    }

    public static Builder builder(){return new Builder();}

    public static class Builder{
        public Column column;

        public Builder(){}

        public Builder column(Column column){
            this.column=column;
            return this;
        }

        public SubIndex build(){
            return new SubIndex(this.column);
        }
    }
}
