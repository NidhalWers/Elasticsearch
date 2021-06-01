package org.snp.dao;

import org.snp.indexage.Table;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TableDao {
    private static List<Table> tables=new ArrayList<>();

    public void insert(Table table){
        tables.add(table);
    }

    public Table find(String key){
        for(Table table : tables){
            if(table.getName().equals(key))
                return table;
        }
        return null;
    }
    public List<Table> findAll(){
        return tables;
    }

    public void delete(String key){
        Table tableToDelete = null;
        for(Table table : tables){
            if(table.getName().equals(key))
                tableToDelete = table;
        }
        if(tableToDelete != null)
            tables.remove(tableToDelete);
    }
}
