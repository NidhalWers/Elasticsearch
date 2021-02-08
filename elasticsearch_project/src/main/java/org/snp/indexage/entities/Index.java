package org.snp.indexage.entities;

import com.google.common.base.Strings;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.binary.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

public class Index {
    private List<Column> columns;
    //Map of value, position list
    private Map<String, ArrayList<String>> index = new TreeMap<>();

    private Index(List<Column> columns) {
        this.columns = columns;
    }

    public void insertLine(HashMap<String,String> data, String reference ) throws Exception{
        ArrayList<String> extractedColKey = new ArrayList();
        for(Column column : columns){
            extractedColKey.add(data.get(column.getName()));
        }
        Collections.sort(extractedColKey);
        String key = String.join(",",extractedColKey);
        if(index.get(key)==null){
            ArrayList newRow = new ArrayList<String>();
            newRow.add(reference);
            index.put(key,newRow);
        }else{
            index.get(key).add(reference);
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
