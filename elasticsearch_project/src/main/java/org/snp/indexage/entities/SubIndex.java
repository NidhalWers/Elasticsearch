package org.snp.indexage.entities;

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

    public boolean deleteByReference(String reference){
        String key =null;
        for(Map.Entry entry :  referenceMap.entrySet()){
            List<String> refList = (List<String>) entry.getValue();
            if(refList.contains(reference)) {
                refList.remove(reference);
                if(refList.isEmpty())
                    key= (String) entry.getKey();
            }
        }
        if(key!=null){
            referenceMap.remove(key);
            return true;
        }
        return false;
    }

    public void updateTheReference(int difference ){

        for(Map.Entry entry : referenceMap.entrySet()){
            List<String> refList = (List<String>) entry.getValue();
            for(int i=0; i<refList.size(); i++){
                String ref = refList.get(i);
                refList.remove(ref);
                String[] split = ref.split(",");
                int oldPos = Integer.valueOf(split[1]);
                int newPos = oldPos + difference;
                refList.add(split[0]+","+newPos+","+split[2]);
            }
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
