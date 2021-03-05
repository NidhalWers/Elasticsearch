package org.snp.utils;

import org.snp.dao.DataDao;
import org.snp.indexage.entities.Table;

import java.util.HashMap;

public class TestUtils {

    private static DataDao dataDao = new DataDao();

    public static Table insertData(Table table){
        HashMap<String, String> data = new HashMap<>();

        data.put("nom", "teyeb");
        data.put("prenom", "nidhal");
        data.put("age", "21");


        try {
            dataDao.insert(table, data,"ligne1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return table;
    }
}
