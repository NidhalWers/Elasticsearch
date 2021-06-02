package org.snp.dao;

import org.snp.indexage.Index;
import org.snp.indexage.Dictionnaire;
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
            Dictionnaire dictionnaire;
            for(String k : keys) {
                dictionnaire =table.getDictionnaireMap().get(k);
                if (dictionnaire !=null) {
                    allResults.add( dictionnaire.find(query.get((k))) );
                }
            }
            List<String> finalResult = ListUtils.intersection(allResults);

            return finalResult;
        }
    }

    public List<String> findAll(Table table){
        ArrayList<String> allResults = new ArrayList<>();
        for(Dictionnaire dictionnaire : table.getDictionnaireMap().values()){
            for(List<String> references : dictionnaire.getReferenceMap().values()){
                for(String ref : references){
                    if(! allResults.contains(ref))
                        allResults.add(ref);
                }
            }
        }

        return allResults;
    }
}
