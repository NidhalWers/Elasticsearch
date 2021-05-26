package org.snp.indexage;

import org.snp.model.credentials.AttributeCredentials;
import org.snp.utils.CompareValue;
import org.snp.utils.ComparisonUtils;


import java.util.*;

public class SubIndex {
    private Column column;

    private Map<String, List<String>> referenceMap = new LinkedHashMap<>();

    ComparisonUtils comparisonUtils = new ComparisonUtils();

    private SubIndex(Column column) {
        this.column = column;
    }

    public void insertLine(String key, String reference){
        if(referenceMap.get(key)==null){
            List refList = new ArrayList();
            refList.add(reference);
            referenceMap.put(key, refList);
        }else {
            if(! referenceMap.get(key).contains(reference))
                referenceMap.get(key).add(reference);
        }
    }

    public Column getColumn() {
        return column;
    }

    public List<String> find(CompareValue compareValue){
        if(compareValue.operator.equals( AttributeCredentials.Operator.EQ))
            return referenceMap.get(compareValue.value);
        else{
            List<String> result = new ArrayList<>();
            for(Map.Entry<String, List<String>> entry : referenceMap.entrySet()){
                if(comparisonUtils.compare(entry.getKey(), compareValue.value, compareValue.operator)){
                    result.addAll( entry.getValue() );
                }
                System.out.println("1");
            }
            return result;
        }
    }

    public boolean deleteByReference(String reference){
        String key =null;
        for(Map.Entry entry :  referenceMap.entrySet()){
            List<String> refList = (List<String>) entry.getValue();
            if(refList.contains(reference)) {
                refList.remove(reference);
                if(refList.isEmpty()) {
                    key = (String) entry.getKey();
                    referenceMap.remove(key);
                }
                return true;
            }
        }
        return false;
    }

    public void updateTheReference(int difference, int from){

        for(Map.Entry entry : referenceMap.entrySet()){
            List<String> refList = (List<String>) entry.getValue();
            List<String> witnessList = new ArrayList<>();
            for(int i=0; i<refList.size(); i++){
                String ref = refList.get(i);
                String[] split = ref.split(",");
                int oldPos = Integer.valueOf(split[1]);
                if(oldPos > from) {
                    int newPos = oldPos + difference;
                    witnessList.add(split[0] + "," + newPos + "," + split[2]);
                }else{
                    witnessList.add(split[0] + "," + oldPos + "," + split[2]);
                }
            }
            refList.clear();
            refList.addAll(witnessList);
            int B = 6;
        }
    }

    public boolean updateByReference(String newKey, String reference){
        boolean delete = deleteByReference(reference);
        if(delete){
            insertLine(newKey, reference);
            return true;
        }
        return false;
    }

    public Map<String, List<String>> getReferenceMap() {
        return referenceMap;
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
