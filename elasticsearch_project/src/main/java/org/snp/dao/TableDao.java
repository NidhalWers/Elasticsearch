package org.snp.dao;

import org.snp.indexage.entities.Table;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class TableDao {
    private static ArrayList<Table> tables=new ArrayList<>();

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
}
