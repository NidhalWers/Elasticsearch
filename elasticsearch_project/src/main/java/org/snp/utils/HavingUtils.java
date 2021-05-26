package org.snp.utils;

import org.snp.model.credentials.HavingCredentials;

import java.util.List;

public class HavingUtils {

    public static HavingCredentials getHavingForFunctionAndColumn(List<HavingCredentials> having, String function, String column){
        for(HavingCredentials havingCredentials : having){
            if(havingCredentials.functionName.equals(function) && havingCredentials.columnName.equals(column))
                return havingCredentials;
        }
        return null;
    }
}
