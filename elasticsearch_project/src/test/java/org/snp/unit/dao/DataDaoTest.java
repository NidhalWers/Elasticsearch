package org.snp.unit.dao;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.snp.dao.DataDao;
import org.snp.indexage.Table;
import org.snp.utils.TestFactory;
import org.snp.utils.TestUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

@QuarkusTest
public class DataDaoTest {


    /**
     * method to test : insertRowIntoIndexes(HashMap<String,String> data, String reference)
     */

    @Test
    public void testInsertRowIntoIndexesHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        Assertions.assertTrue(table.getSubIndexMap().get("nom").find("teyeb").contains("ligne1"));
    }

    @Test
    public void testInsertRowIntoSubIndexSadPath() {
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        Assertions.assertFalse(table.getSubIndexMap().get("nom").find("oruc") != null);
    }

    @Test
    public void testInsertRowIntoIndexOneColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String,String> query = new HashMap<>();
        query.put("nom","teyeb");
        Assertions.assertTrue(table.getIndexes().get("nom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexTwoColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String,String> query = new HashMap<>();
        query.put("nom","teyeb");
        query.put("prenom","nidhal");
        Assertions.assertTrue(table.getIndexes().get("nom,prenom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexMultipleColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String,String> query = new HashMap<>();
        query.put("nom","teyeb");
        query.put("prenom","nidhal");
        query.put("age", "21");
        Assertions.assertTrue(table.getIndexes().get("age,nom,prenom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexSadPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        Assertions.assertFalse(table.getSubIndexMap().get("nom").find("oruc") != null);
    }



    /**
     * method to test : find(Table table, HashMap<String,String> query )
     */

    @Inject
    DataDao dataDao;

    @Test
    public void testExecuteQueryOneColumnHappyPath(){
        Table table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");

        List<String> values = dataDao.find(table,query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQueryMultipleColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");
        query.put("prenom", "nidhal");

        List<String> values = dataDao.find(table,query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQuerySadPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");

        List<String> values = dataDao.find(table,query);
        Assertions.assertFalse(values.contains("ligne2"));
    }


}
