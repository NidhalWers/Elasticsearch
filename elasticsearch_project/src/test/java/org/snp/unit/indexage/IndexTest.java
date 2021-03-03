package org.snp.unit.indexage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import io.quarkus.test.junit.mockito.InjectMock;
import org.snp.TestFactory;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.helpers.SubIndex;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class IndexTest {


    /**
     * method to test insertLine(HashMap<String,String> data, String reference )
     */

    @InjectSpy
    SubIndex subIndex;



    @Test
    public void testInsertLineHappyPath(){

        Index index = TestFactory.createIndexNom();

        List<String> list = new ArrayList<>();
        list.add("ligne1");
        Mockito.when(subIndex.find(any())).thenReturn(list);


        HashMap<String, String> data = new HashMap<>();
        data.put("nom", "teyeb");
        data.put("prenom", "nidhal");
        data.put("age", "21");

        index.insertLine(data, "ligne1");

        HashMap<String,String> query = new HashMap<>();
        query.put("nom","teyeb");

        Assertions.assertTrue(index.find(query).contains("ligne1"));

   }


    @Test
    public void testInsertLineSadPath(){

       Index index = TestFactory.createIndexNom();

        List<String> list = new ArrayList<>();
        list.add("ligne1");
        Mockito.when(subIndex.find(any())).thenReturn(list);

        HashMap<String, String> data = new HashMap<>();
        data.put("nom", "oruc");
        data.put("prenom", "sinem");
        data.put("age", "22");

        index.insertLine(data, "ligne1");

        HashMap<String,String> query = new HashMap<>();
        query.put("nom", "oruc");

        Assertions.assertFalse(index.find(query).contains("ligne2"));


    }

    /**
     * method to test : find(HashMap<String,String> query)
     */

    @Test
    public void testFindHappyPath(){

    }

    @Test
    public void testFindSadPath(){

    }
}
