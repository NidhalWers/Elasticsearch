package org.snp.unit.indexage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.snp.indexage.entities.Column;

public class ColumnTest {

    /**
     * method to test : getName()
     */
    @Test
    public void testGetNameHappyPath(){
        Column column = Column.builder()
                                .name("nom")
                                .build();

        Assertions.assertEquals("nom", column.getName());

    }

    @Test
    public void testGetNameSadPath(){
        Column column = Column.builder()
                .name("nom")
                .build();

        Assertions.assertNotEquals("prenom", column.getName());
    }

    /**
     * method to test : getType()
     */

    @Test
    public void testGetTypeHappyPath(){
        Column column = Column.builder()
                .type("String")
                .build();

        Assertions.assertEquals("String", column.getType());
    }

    @Test
    public void testGetTypeSadPath(){
        Column column = Column.builder()
                .type("String")
                .build();

        Assertions.assertNotEquals("Double", column.getType());
    }

    /**
     * method to test : toString()
     */

    @Test
    public void testToStringHappyPath(){
        Column column = Column.builder()
                .type("String")
                .name("nom")
                .build();

        String test = "Column{name='nom', type='String'}";
        Assertions.assertEquals(column.toString(), test);
    }

    @Test
    public void testToStringSadPath(){
        Column column = Column.builder()
                .type("String")
                .name("nom")
                .build();

        String test = "Column{" +
                "name=" + "prenom" + '\'' +
                ", type=" + "String" + '\'' +
                '}';
        Assertions.assertNotEquals(column.toString(), test);
    }

    /**
     * method to test : equals(Object o)
     */

    @Test
    public void testEqualsHappyPath(){
        Column column1 = Column.builder()
                .type("String")
                .name("nom")
                .build();
        Column column2 = Column.builder()
                .type("String")
                .name("nom")
                .build();

        Assertions.assertEquals(column1, column2);
    }

    @Test
    public void testEqualsSadPath(){
        Column column1 = Column.builder()
                .type("String")
                .name("nom")
                .build();
        Column column2 = Column.builder()
                .type("String")
                .name("prenom")
                .build();

        Assertions.assertNotEquals(column1, column2);
    }



}
