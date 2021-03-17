package org.snp.dao;

import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.SubIndex;
import org.snp.indexage.entities.Table;

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

    public List<String> find(Table table, HashMap<String,String> query ){
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
            return null;
            //todo verifier si il y a un index pour chaque colonne et faire l'intersection
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

    public void delete(Table table, HashMap<String, String> data, String reference){

    }

    public void update(Table table, HashMap<String,String> query, HashMap<String, String> data, String reference){
        ArrayList<String> keys = new ArrayList<>();
        for(String key : query.keySet()){
            keys.add(key);
        }
        Collections.sort(keys);
        String indexKey = String.join(",",keys);
        Index index = table.getIndexes().get(indexKey);

        //todo
    }
}
