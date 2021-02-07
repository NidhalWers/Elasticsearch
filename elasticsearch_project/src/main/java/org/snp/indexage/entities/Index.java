package org.snp.indexage.entities;

import org.snp.utils.StringUtils;

import java.util.*;

public class Index {
    private Map<String, ArrayList<String>> index = new TreeMap<>();
    private List<Column> columns = new ArrayList<>();

    public void insert(String value, String ...keys ){
        String key = StringUtils.getKeyFromTab(keys);
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

}
