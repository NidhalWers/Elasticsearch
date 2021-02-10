package org.snp.unit;


import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@QuarkusTest
public class IndexageTest {

    static Table table;
    static HashMap<String, String> data = new HashMap<>();

    @BeforeAll
    public static void initTableToTest() throws Exception {
        table = Table
                .builder()
                .name("test")
                .build();

        table.addColumn(Column.builder()
                .type("string")
                .name("nom")
                .build());
        table.addColumn(Column.builder()
                .name("prenom")
                .type("string")
                .build());
        table.addColumn(Column.builder()
                .name("age")
                .type("int")
                .build());

        //index on nom
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(Column.builder()
                .name("nom")
                .type("string")
                .build());

        table.createIndex(columns);
        //index on nom & prenom
        ArrayList<Column> columns2 = new ArrayList<>();
        columns2.add(Column.builder()
                .name("nom")
                .type("string")
                .build());
        columns2.add(Column.builder()
                .name("prenom")
                .type("string")
                .build());

        table.createIndex(columns2);


        data.put("nom", "teyeb");
        data.put("prenom", "nidhal");
        data.put("age", "21");


        table.insertRowIntoIndexes(data,"ligne1");
    }

    @Test
    public void testInsertRowIntoSubIndexHappyPath(){
        Assertions.assertTrue(table.getSubIndexMap().get("nom").find("teyeb").contains("ligne1"));
    }

    @Test
    public void testInsertRowIntoSubIndexSadPath() {
        Assertions.assertFalse(table.getSubIndexMap().get("nom").find("oruc") != null);
    }

    @Test
    public void testInsertRowIntoIndexOneColumnHappyPath(){
        HashMap<String,String> query = new HashMap<>();
        query.put("nom","teyeb");
        Assertions.assertTrue(table.getIndexes().get("nom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexMultipleColumnHappyPath(){
        HashMap<String,String> query = new HashMap<>();
        query.put("nom","teyeb");
        query.put("prenom","nidhal");
        Assertions.assertTrue(table.getIndexes().get("nom,prenom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexSadPath(){
        Assertions.assertFalse(table.getSubIndexMap().get("nom").find("oruc") != null);
    }





    @Test
    public void testExecuteQueryOneColumnHappyPath(){
        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");

        List<String> values = table.executeQuery(query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQueryMultipleColumnHappyPath(){
        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");
        query.put("prenom", "nidhal");

        List<String> values = table.executeQuery(query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQuerySadPath(){
        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");

        List<String> values = table.executeQuery(query);
        Assertions.assertFalse(values.contains("ligne2"));
    }

}
