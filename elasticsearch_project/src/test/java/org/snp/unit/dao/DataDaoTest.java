package org.snp.unit.dao;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.dao.DataDao;
import org.snp.indexage.Table;
import org.snp.model.credentials.AttributeCredentials;
import org.snp.utils.CompareValue;
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

        Assertions.assertTrue(table.getSubIndexMap().get("nom").find(CompareValue.builder()
                                                                    .value("teyeb")
                                                                    .comparison(AttributeCredentials.Operator.EQ)
                                                                    .build())
                                                                .contains("ligne1"));
    }

    @Test
    public void testInsertRowIntoSubIndexSadPath() {
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        Assertions.assertFalse(table.getSubIndexMap().get("nom").find(CompareValue.builder()
                                                                                .value("oruc")
                                                                                .comparison(AttributeCredentials.Operator.EQ)
                                                                                .build())
                                                                            != null);
    }

    @Test
    public void testInsertRowIntoIndexOneColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String,CompareValue> query = new HashMap<>();
        query.put("nom",CompareValue.builder()
                .value("teyeb")
                .comparison(AttributeCredentials.Operator.EQ)
                .build());
        Assertions.assertTrue(table.getIndexes().get("nom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexTwoColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String,CompareValue> query = new HashMap<>();
        query.put("nom",CompareValue.builder()
                        .value("teyeb")
                        .comparison(AttributeCredentials.Operator.EQ)
                        .build());
        query.put("prenom",CompareValue.builder()
                        .value("nidhal")
                        .comparison(AttributeCredentials.Operator.EQ)
                        .build());
        Assertions.assertTrue(table.getIndexes().get("nom,prenom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexMultipleColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String,CompareValue> query = new HashMap<>();
        query.put("nom",CompareValue.builder()
                        .value("teyeb")
                        .comparison(AttributeCredentials.Operator.EQ)
                        .build());
        query.put("prenom",CompareValue.builder()
                .value("nidhal")
                .comparison(AttributeCredentials.Operator.EQ)
                .build());
        query.put("age", CompareValue.builder()
                .value("21")
                .comparison(AttributeCredentials.Operator.EQ)
                .build() );
        Assertions.assertTrue(table.getIndexes().get("age,nom,prenom").find(query).contains("ligne1"));

    }

    @Test
    public void testInsertRowIntoIndexSadPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        Assertions.assertFalse(table.getSubIndexMap().get("nom").find(CompareValue.builder()
                                                                                .value("oruc")
                                                                                .comparison(AttributeCredentials.Operator.EQ)
                                                                                .build())
                                                                        != null);
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

        HashMap<String, CompareValue> query = new HashMap<>();
        query.put("nom", CompareValue.builder()
                .value("teyeb")
                .comparison(AttributeCredentials.Operator.EQ)
                .build());

        List<String> values = dataDao.find(table,query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQueryOneColumnSuperiorHappyPath(){
        Table table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, CompareValue> query = new HashMap<>();
        query.put("age", CompareValue.builder()
                .value("15")
                .comparison(AttributeCredentials.Operator.SUP)
                .build());

        List<String> values = dataDao.find(table,query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQueryMultipleColumnHappyPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, CompareValue> query = new HashMap<>();
        query.put("nom", CompareValue.builder()
                .value("teyeb")
                .comparison(AttributeCredentials.Operator.EQ)
                .build());
        query.put("prenom", CompareValue.builder()
                .value("nidhal")
                .comparison(AttributeCredentials.Operator.EQ)
                .build());

        List<String> values = dataDao.find(table,query);
        Assertions.assertTrue(values.contains("ligne1"));
    }

    @Test
    public void testExecuteQuerySadPath(){
        Table  table = TestFactory.createTable();
        table = TestUtils.insertData(table);

        HashMap<String, CompareValue> query = new HashMap<>();
        query.put("nom", CompareValue.builder()
                .value("teyeb")
                .comparison(AttributeCredentials.Operator.EQ)
                .build());

        List<String> values = dataDao.find(table,query);
        Assertions.assertFalse(values.contains("ligne2"));
    }


}
