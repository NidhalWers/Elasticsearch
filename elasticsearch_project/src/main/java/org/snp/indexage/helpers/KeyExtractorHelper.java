package org.snp.indexage.helpers;


import java.util.*;

public class KeyExtractorHelper {

    public static String extractKeyStringFromHashMap(HashMap<String,?> data){
        Set<String> keys = data.keySet();
        List<String> listKeys= new ArrayList<>(keys);
        Collections.sort(listKeys);
        String key = String.join(",",listKeys);
        return key;
    }
}
