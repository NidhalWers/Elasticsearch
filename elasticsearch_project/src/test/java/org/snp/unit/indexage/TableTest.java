package org.snp.unit.indexage;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.snp.indexage.Column;
import org.snp.service.TableService;
import org.snp.utils.TestFactory;
import org.snp.indexage.Table;

import java.util.List;



import javax.inject.Inject;


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
    @Inject
    TableService tableService;

    @Test
    public void testCreateIndexOneColumn(){
        List<Column> columns = TestFactory.createListColumn(List.of("nom","prenom","age"));

        Table table = Table
                .builder()
                .name("test")
                .columns(columns)
                .build();

        tableService.addIndex(table, TestFactory
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

        tableService.addIndex(table, TestFactory
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
