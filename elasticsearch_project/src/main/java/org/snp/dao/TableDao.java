package org.snp.dao;

import org.snp.indexage.entities.Table;

import java.util.ArrayList;

public class TableDao {
    private ArrayList<Table> tables=new ArrayList<>();

    public void insert(Table table){
        tables.add(table);
    }

    public Table find(String key){
        for(Table table : tables){
            if( table.getName().equals(key))
                return table;
        }
        return null;
    }
}
