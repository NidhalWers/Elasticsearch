package org.snp.utils;

import org.snp.Main;
import org.snp.httpclient.SlaveClient;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.AttributeCredentials;
import org.snp.model.credentials.FunctionCredentials;
import org.snp.service.data.FunctionService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class FunctionUtils {

    public boolean isValideFunction(String functionName){
        return "None".equals(functionName) ||
                "sum".equals(functionName) ||
                "avg".equals(functionName) ||
                "min".equals(functionName) ||
                "max".equals(functionName) ||
                "count".equals(functionName)
                ;
    }

    @Inject
    FunctionService functionService;

    private SlaveClient masterClient = new SlaveClient(8080);

    public Message switchFunction(String functionName, String tableName, String columnName, List<AttributeCredentials> queryParams){
        if(! Main.isMasterTest()){
            List<String> masterResultInList = masterClient.functions(FunctionCredentials.builder()
                                    .tableName(tableName)
                                    .aggregate(functionName, columnName)
                                    .queryParams(queryParams)
                                    .build());
            return new MessageAttachment<>(200, masterResultInList.get(0));
        }else {
            switch (functionName) {
                case "sum":
                    return functionService.sum(tableName, columnName, queryParams);
                case "avg":
                    return functionService.avg(tableName, columnName, queryParams);
                case "min":
                    return functionService.min(tableName, columnName, queryParams);
                case "max":
                    return functionService.max(tableName, columnName, queryParams);
                case "count":
                    if (columnName != null)
                        return functionService.count(tableName, columnName, queryParams);
                    else
                        return functionService.count(tableName, queryParams);
                default:
                    return new MessageAttachment<>(404, "function_name does not correspond");
            }
        }
    }
}
