package org.snp.dao;

import org.snp.indexage.Index;
import org.snp.indexage.SubIndex;
import org.snp.indexage.Table;
import org.snp.utils.CompareValue;
import org.snp.utils.ListUtils;

import javax.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class DataDao {
    public void insert(Table table, HashMap<String, String> data, String reference){
        Set<String> keys = table.getIndexes().keySet();
        for(String key : keys){
            table.getIndexes().get(key).insertLine(data,reference);
        }
    }

    public List<String> find(Table table, HashMap<String, CompareValue> query ){
        ArrayList<String> keys = new ArrayList<>();
        for(String key : query.keySet()){
            keys.add(key);
        }
        Collections.sort(keys);
        String indexKey = String.join(",",keys);
        Index index = table.getIndexes().get(indexKey);
        if(index != null)
            return index.find(query);
        else {
            List<List> allResults = new ArrayList<>();
            SubIndex subIndex;
            for(String k : keys) {
                subIndex=table.getSubIndexMap().get(k);
                if (subIndex!=null) {
                    allResults.add( subIndex.find(query.get((k))) );
                }
            }
            List<String> finalResult = ListUtils.intersection(allResults);

            return finalResult;
        }
    }

    public List<String> findAll(Table table){
        ArrayList<String> allResults = new ArrayList<>();
        for(SubIndex subIndex : table.getSubIndexMap().values()){
            for(List<String> references : subIndex.getReferenceMap().values()){
                for(String ref : references){
                    if(! allResults.contains(ref))
                        allResults.add(ref);
                }
            }
        }

        return allResults;
    }
}
