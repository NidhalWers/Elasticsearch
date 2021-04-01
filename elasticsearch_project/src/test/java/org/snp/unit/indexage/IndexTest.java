package org.snp.unit.indexage;


import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.indexage.Column;
import org.snp.indexage.SubIndex;
import org.snp.indexage.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@QuarkusTest
public class IndexTest {


    /**
     * method to test insertLine(HashMap<String,String> data, String reference )
     */



    @Test
    public void testInsertLineHappyPath(){

        Map<String, SubIndex> subIndexMap = new HashMap<>();
        SubIndex subIndex = SubIndex.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();
        subIndexMap.put("nom",subIndex );

        ArrayList<Column> column = new ArrayList<>();
        column.add(Column.builder()
                .name("nom")
                .type("String")
                .build());
        //créer la map
        Index index = Index.builder()
                .columns(column)
                .subIndexes(subIndexMap)
                .build();


        HashMap<String, String> data = new HashMap<>();
        data.put("nom", "oruc");

        index.insertLine(data, "ligne1");

        HashMap<String,String> query = new HashMap<>();
        query.put("nom", "oruc");

        //Assertions.assertFalse(index.find(query).contains("ligne2"));
        Assertions.assertTrue(subIndex.find("oruc").contains("ligne1"));

   }


    /**
     * method to test : find(HashMap<String,String> query)
     */

    @Test
    public void testFindHappyPath(){
        Map<String, SubIndex> subIndexMap = new HashMap<>();
        SubIndex subIndex = SubIndex.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();
        subIndexMap.put("nom",subIndex );

        ArrayList<Column> column = new ArrayList<>();
        column.add(Column.builder()
                .name("nom")
                .type("String")
                .build());
        //créer la map
        Index index = Index.builder()
                .columns(column)
                .subIndexes(subIndexMap)
                .build();

        subIndex.insertLine("oruc", "ligne2");


        HashMap<String,String> query = new HashMap<>();
        query.put("nom", "oruc");

        List<String> resultTest = subIndex.find("oruc");
        Assertions.assertTrue(resultTest.contains("ligne2"));
    }
}
