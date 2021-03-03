package org.snp.unit.indexage;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.snp.TestFactory;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.snp.indexage.helpers.SubIndex;


@QuarkusTest
public class TableTest {


        /**
         * method to test : insertRowIntoIndexes(HashMap<String,String> data, String reference)
         */

        /*
        @InjectSpy
        SubIndex subIndex;

        @Test
        public void testInsertRowIntoIndexesHappyPath(){

            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, String> data = new HashMap<>();

            data.put("nom", "teyeb");
            data.put("prenom", "nidhal");
            data.put("age", "21");


            try {
                table.insertRowIntoIndexes(data,"ligne1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Assertions.assertTrue(table.getSubIndexMap().get("nom").find("teyeb").contains("ligne1"));
        }

        @Test
        public void testInsertRowIntoSubIndexSadPath() {

            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, String> data = new HashMap<>();

            data.put("nom", "teyeb");
            data.put("prenom", "nidhal");
            data.put("age", "21");


            try {
                table.insertRowIntoIndexes(data,"ligne1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Assertions.assertFalse(table.getSubIndexMap().get("nom").find("oruc") != null);
        }

        @Test
        public void testInsertRowIntoIndexOneColumnHappyPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String,String> query = new HashMap<>();
            query.put("nom","teyeb");
            Assertions.assertTrue(table.getIndexes().get("nom").find(query).contains("ligne1"));

        }

        @Test
        public void testInsertRowIntoIndexTwoColumnHappyPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String,String> query = new HashMap<>();
            query.put("nom","teyeb");
            query.put("prenom","nidhal");
            Assertions.assertTrue(table.getIndexes().get("nom,prenom").find(query).contains("ligne1"));

        }

        @Test
        public void testInsertRowIntoIndexMultipleColumnHappyPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String,String> query = new HashMap<>();
            query.put("nom","teyeb");
            query.put("prenom","nidhal");
            query.put("age", "21");
            Assertions.assertTrue(table.getIndexes().get("age,nom,prenom").find(query).contains("ligne1"));

        }

        @Test
        public void testInsertRowIntoIndexSadPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Assertions.assertFalse(table.getSubIndexMap().get("nom").find("oruc") != null);
        }





        @Test
        public void testExecuteQueryOneColumnHappyPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, String> query = new HashMap<>();
            query.put("nom", "teyeb");

            List<String> values = table.executeQuery(query);
            Assertions.assertTrue(values.contains("ligne1"));
        }

        @Test
        public void testExecuteQueryMultipleColumnHappyPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, String> query = new HashMap<>();
            query.put("nom", "teyeb");
            query.put("prenom", "nidhal");

            List<String> values = table.executeQuery(query);
            Assertions.assertTrue(values.contains("ligne1"));
        }

        @Test
        public void testExecuteQuerySadPath(){
            Table table= null;
            try {
                table = TestFactory.createTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, String> query = new HashMap<>();
            query.put("nom", "teyeb");

            List<String> values = table.executeQuery(query);
            Assertions.assertFalse(values.contains("ligne2"));
        }


         */


}
