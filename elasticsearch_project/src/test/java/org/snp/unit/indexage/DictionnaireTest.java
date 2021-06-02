package org.snp.unit.indexage;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.indexage.Column;
import org.snp.indexage.Dictionnaire;
import org.snp.model.credentials.AttributeCredentials;
import org.snp.utils.CompareValue;

@QuarkusTest
public class DictionnaireTest {

    /**
     * method to test : getColumn()
     */
    @Test
    public void testGetColumnHappyPath(){
        Column column = Column.builder()
                            .name("nom")
                            .type("String")
                            .build();

        Dictionnaire dictionnaire = Dictionnaire.builder()
                                    .column(column)
                                    .build();

        Assertions.assertEquals(dictionnaire.getColumn(), column );

    }

    @Test
    public void testGetColumnSadPath(){
        Column column = Column.builder()
                                .name("nom")
                                .type("String")
                                .build();

        Dictionnaire dictionnaire = Dictionnaire.builder()
                                    .column(column)
                                    .build();

        Assertions.assertNotEquals(dictionnaire.getColumn(), Column.builder()
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

        Dictionnaire dictionnaire = Dictionnaire.builder()
                                    .column(Column.builder()
                                                .name("nom")
                                                .type("String")
                                                .build())
                                    .build();

        dictionnaire.insertLine("oruc", "ligne1");

        Assertions.assertTrue(dictionnaire.find(CompareValue.builder()
                                            .value("oruc")
                                            .comparison(AttributeCredentials.Operator.EQ)
                                            .build())
                                    .contains("ligne1"));
    }

    @Test
    public void testInsertLineSadPath() {
        Dictionnaire dictionnaire = Dictionnaire.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();

        dictionnaire.insertLine("teyeb", "ligne1");

        Assertions.assertFalse(dictionnaire.find(CompareValue.builder()
                                                        .value("oruc")
                                                        .comparison(AttributeCredentials.Operator.EQ)
                                                        .build())
                                                !=null);
    }


    /**
     * method to test : find(String key)
     */

    @Test
    public void testFindHappyPath(){
        Dictionnaire dictionnaire = Dictionnaire.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();

        dictionnaire.insertLine("oruc", "ligne1");

        Assertions.assertTrue(dictionnaire.find(CompareValue.builder()
                                            .value("oruc")
                                            .comparison(AttributeCredentials.Operator.EQ)
                                            .build())
                                    .contains("ligne1"));
    }

    @Test
    public void testFindSaddPath(){
        Dictionnaire dictionnaire = Dictionnaire.builder()
                .column(Column.builder()
                        .name("nom")
                        .type("String")
                        .build())
                .build();


        Assertions.assertFalse(dictionnaire.find(CompareValue.builder()
                                                        .value("oruc")
                                                        .comparison(AttributeCredentials.Operator.EQ)
                                                        .build())
                                                !=null);
    }




}
