package org.snp.dao;

import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;

import java.util.*;

public class DataDao {

    public void insertRowIntoIndexes(Table table, HashMap<String, String> data, String reference)throws Exception{
        Map<String, Index> indexes = table.getIndexes();
        Set<String> keys = indexes.keySet();
        for(String key : keys){
            indexes.get(key).insertLine(data,reference);
        }
    }

    public List<String> executeQuery(Table table, HashMap<String,String> query ){
        Map<String, Index> indexes = table.getIndexes();
        ArrayList<String> keys = new ArrayList<>();
        for(String key : query.keySet()){
            keys.add(key);
        }
        Collections.sort(keys);
        String indexKey = String.join(",",keys);
        return indexes.get(indexKey).find(query);
    }
}