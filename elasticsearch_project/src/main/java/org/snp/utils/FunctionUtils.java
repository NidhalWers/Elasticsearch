package org.snp.utils;

import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.AttributeCredentials;
import org.snp.service.data.FunctionService;

import javax.inject.Inject;
import java.util.List;

public class FunctionUtils {

    public static boolean isValideFunction(String functionName){
        return "None".equals(functionName) ||
                "sum".equals(functionName) ||
                "avg".equals(functionName) ||
                "min".equals(functionName) ||
                "max".equals(functionName) ||
                "count".equals(functionName)
                ;
    }

    @Inject
    static FunctionService functionService;

    public static Message switchFunction(String functionName, String tableName, String columnName, List<AttributeCredentials> queryParams){
        switch (functionName){
            case "sum" :
                return functionService.sum(tableName, columnName, queryParams);
            case "avg" :
                return functionService.avg(tableName, columnName, queryParams);
            case "min" :
                return functionService.min(tableName, columnName, queryParams);
            case "max" :
                return functionService.max(tableName, columnName, queryParams);
            case "count" :
                if( columnName != null)
                    return functionService.count(tableName, columnName, queryParams);
                else
                    return functionService.count(tableName, queryParams);
            default:
                return new MessageAttachment<>(404, "function_name does not correspond");
        }
    }
}
