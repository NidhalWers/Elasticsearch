package org.snp.service.data;

import org.snp.dao.DataDao;
import org.snp.dao.TableDao;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.query.AttributeCredentials;
import org.snp.model.credentials.query.QueryCredentials;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class DataService {

    @Inject FileService fileService;

    private TableDao tableDao = new TableDao();
    private DataDao dataDAO = new DataDao();


    public Message query(QueryCredentials queryCredentials){
        Table table = tableDao.find(queryCredentials.tableName);
        if(table == null)
            return new MessageAttachment<>(404, "table "+ queryCredentials.tableName+" does not exists");

        HashMap<String, String> queryMap = new HashMap<>();
        for(AttributeCredentials attributeCredentials : queryCredentials.queryParams){
            String columnName = attributeCredentials.name;
            if(! table.containsColumn(columnName))
                return new MessageAttachment<>(404, "column "+columnName+" does not exists in "+ queryCredentials.tableName);
            queryMap.put(attributeCredentials.name, attributeCredentials.value);
        }

        List<String> references = dataDAO.find(table, queryMap);
        if(references == null || references.isEmpty())
            return new MessageAttachment<>(404, "data not found");

        List<String> values = new ArrayList<>();
        for(String ref : references){
            String[] refSplited = ref.split(",");
            values.add(fileService.getAllDataAtPos(refSplited[0], Integer.valueOf(refSplited[1]), Integer.valueOf(refSplited[2])) );
        }

        return new MessageAttachment<List>(200, values);
    }






    }
