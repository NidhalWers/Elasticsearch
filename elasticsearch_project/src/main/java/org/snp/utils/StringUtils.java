package org.snp.utils;

public class StringUtils {

    public static String getKeyFromTab(String ...strings){
        int len = strings.length;
        String tmp;
        for(int i=0; i<len; i++){
            for(int j=i+1; j<len; j++){
                if(strings[i].compareToIgnoreCase(strings[j])>0){
                    tmp = strings[i];
                    strings[i]=strings[j];
                    strings[j]=tmp;
                }
            }
        }
        String key = "";
        for(String k : strings ){
            key+=k;
        }

        return key;
    }
}
