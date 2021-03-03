package org.snp.utils;

import org.snp.indexage.entities.Table;

import java.util.HashMap;

public class TestUtils {

    public static Table insertData(Table table){
        HashMap<String, String> data = new HashMap<>();

        data.put("nom", "teyeb");
        data.put("prenom", "nidhal");
        data.put("age", "21");


        try {
            table.insertRowIntoIndexes(data,"ligne1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return table;
    }
}
