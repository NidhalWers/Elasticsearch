package org.snp.unit.indexage;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.indexage.entities.Column;
import org.snp.utils.TestFactory;
import org.snp.indexage.entities.Table;

import java.util.HashMap;
import java.util.List;


import org.snp.utils.TestUtils;


@QuarkusTest
public class TableTest {

    /**
     * method to test : getName()
     */
    @Test
    public void testGetNameHappyPath(){
        Table table = Table
                .builder()
                .name("test")
                .build();

        Assertions.assertEquals("test", table.getName());
    }
    @Test
    public void testGetNameSadPath(){
        Table table = Table
                .builder()
                .name("test")
                .build();
        Assertions.assertNotEquals("", table.getName());
    }

    /**
     * method to test : addColumn(Column column)
     */
    @Test
    public void testAddColumnHappyPath(){
        Table table = Table
                .builder()
                .name("test")
                .build();
        Column column = Column.builder()
                .type("string")
                .name("nom")
                .build();
        table.addColumn(column);

        Assertions.assertTrue(table.getColumns().contains(column));
    }
    @Test
    public void testAddColumnSadPath(){
        Table table = Table
                .builder()
                .name("test")
                .build();
        Column column = Column.builder()
                .type("string")
                .name("nom")
                .build();
        table.addColumn(column);

        Assertions.assertFalse(table.getColumns().contains(Column.builder()
                                                                .name("prenom")
                                                                .build()
                                                            ));
    }
    /**
     * method to test : removeColumn(Column column)
     *///todo test remove of the subindex & index with this column
    @Test
    public void testRemoveColumnHappyPath(){
        Table table = Table
                .builder()
                .name("test")
                .build();
        Column column = Column.builder()
                .type("string")
                .name("nom")
                .build();
        table.addColumn(column);

        table.removeColumn(column);

        Assertions.assertFalse(table.getColumns().contains(column));
    }

    /**
     * method to test : getColumns()
     */
    @Test
    public void testGetColumns(){
        List<Column> columns = TestFactory.createListColumn(List.of("nom","prenom","age"));

        Table table = Table
                .builder()
                .name("test")
                .columns(columns)
                .build();

        Assertions.assertEquals(columns, table.getColumns());
    }

    /**
     * method to test : createIndex(List<Column> cols)
     */
    @Test
    public void testCreateIndexOneColumn(){
        List<Column> columns = TestFactory.createListColumn(List.of("nom","prenom","age"));

        Table table = Table
                .builder()
                .name("test")
                .columns(columns)
                .build();

        table.createIndex(TestFactory
                            .createListColumn(
                                    List.of("nom")
                            )
        );

        Assertions.assertTrue(table.getIndexes().get("nom")!=null);
    }

    @Test
    public void testCreateIndexTwoColumn(){
        List<Column> columns = TestFactory.createListColumn(List.of("nom","prenom","age"));

        Table table = Table
                .builder()
                .name("test")
                .columns(columns)
                .build();

        table.createIndex(TestFactory
                .createListColumn(
                        List.of("nom","prenom")
                )
        );

        Assertions.assertTrue(table.getIndexes().get("nom,prenom")!=null);
    }

    @Test
    public void testCreateIndexSadPath(){
        List<Column> columns = TestFactory.createListColumn(List.of("nom","prenom","age"));

        Table table = Table
                .builder()
                .name("test")
                .columns(columns)
                .build();

       Assertions.assertTrue(table.getIndexes().get("nom")==null);
    }

    /**
     * method to test : removeIndex(Index index)
     */
    @Test
    public void testRemoveIndex(){
        //todo remove with a list of column's name
        Table table = TestFactory.createTable();

    }


    /**
     * method to test : getIndexes()
     */
    @Test
    public void testGetIndexes(){
        Table table = TestFactory.createTable();

        Assertions.assertTrue(table.getIndexes()!=null);
    }

    /**
     * method to test : getSubIndexMap()
     */
    @Test
    public void testGetSubIndexMap(){
        Table table = TestFactory.createTable();
        Assertions.assertTrue(table.getSubIndexMap()!=null);
    }

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
     * method to test : executeQuery(HashMap<String,String> query )
     */

/*
    @Test
    public void testExecuteQueryOneColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");

        List<String> values = table.executeQuery(query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQueryMultipleColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");
        query.put("prenom", "nidhal");

        List<String> values = table.executeQuery(query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQuerySadPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, String> query = new HashMap<>();
        query.put("nom", "teyeb");

        List<String> values = table.executeQuery(query);
        Assertions.assertFalse(values.contains("ligne2"));
    }
*/
    /**
     * method to test : equals(Object o)
     */
    @Test
    public void testEqualsHappyPath(){
        Table table1 = TestFactory.createTable();
        Table table2 = TestFactory.createTable();

        Assertions.assertEquals(table1,table2);
    }

    @Test
    public void testEqualsSadPath(){
        Table table1 = TestFactory.createTable();
        Table table2 = Table.builder()
                            .columns(TestFactory.createListColumn( List.of("nom", "prenom", "age") ))
                            .name("no test")
                            .build();

        Assertions.assertNotEquals(table1,table2);
    }





}
