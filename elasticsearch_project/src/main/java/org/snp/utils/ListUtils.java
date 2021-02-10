package org.snp.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static List intersection(List<List> lists){
        List newList = new ArrayList();
        boolean first=true;
        for(List aList : lists){
            if(first){
                newList.addAll(aList);
                first=false;
            }else{
                newList.retainAll(aList);
            }

        }
        return newList;
    }
}
