package org.snp.unit.indexage;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.indexage.entities.Column;
import org.snp.indexage.helpers.SubIndex;

import java.util.ArrayList;
import java.util.HashMap;

@QuarkusTest
public class SubIndexTest {

    /**
     * method to test : getColumn()
     */
    @Test
    public void testGetColumnHappyPath(){
        Column column = Column.builder()
                            .name("nom")
                            .type("String")
                            .build();

        SubIndex subIndex = SubIndex.builder()
                                    .column(column)
                                    .build();

        Assertions.assertEquals(subIndex.getColumn(), column );

    }

    @Test
    public void testGetColumnSadPath(){
        Column column = Column.builder()
                                .name("nom")
                                .type("String")
                                .build();

        SubIndex subIndex = SubIndex.builder()
                                    .column(column)
                                    .build();

        Assertions.assertNotEquals(subIndex.getColumn(), Column.builder()
                                                                .name("prenom")
                                                                .type("String")
                                                                .build()
        );

    }

    /**
     * method to test : insertLine(String key, String reference)
     */

    @Test
    public void testInsertLineHappyPath(){

        SubIndex subIndex = SubIndex.builder()
                                    .column(Column.builder()
                                                .name("nom")
                                                .type("String")
                                                .build())
                                    .build();

        subIndex.insertLine("oruc", "ligne1");

        Assertions.assertTrue(subIndex.find("oruc").contains("ligne1"));
    }

    @Test
    public void testInsertLineSadPath() {
        SubIndex subIndex = SubIndex.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();

        subIndex.insertLine("teyeb", "ligne1");

        Assertions.assertFalse(subIndex.find("oruc").contains("ligne1"));
    }


    /**
     * method to test : find(String key)
     */

    @Test
    public void testFindHappyPath(){
        SubIndex subIndex = SubIndex.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();

        subIndex.insertLine("oruc", "ligne1");

        Assertions.assertTrue(subIndex.find("oruc").contains("ligne1"));
    }

    @Test
    public void testFindSaddPath(){
        SubIndex subIndex = SubIndex.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();


        Assertions.assertFalse(subIndex.find("oruc").contains("ligne1"));
    }




}
