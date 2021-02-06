package org.snp.unit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class CreateTableTest {
    Table table;

    @BeforeAll
    public void initTableToTest(){
        table = Table
                .builder()
                .name("test")
                .build();

        table.addColumn(Column.builder()
                .name("column1")
                .type("string")
                .build());
        table.addColumn(Column.builder()
                .name("column2")
                .type("double")
                .build());
        table.addColumn(Column.builder()
                .name("column3")
                .type("int")
                .build());
    }


    @Test
    public void testCreateTableFirstTime(){


        given()
            .when().post("/elasticsearch/createtablewithparam?name=test&columns=column1,string;column2,double;column3,int")
            .then()
            .statusCode(200)
            .body(is(table.toString()));
    }

    @Test
    public void testCreateTableSecondeTime(){
        given()
                .when().post("/elasticsearch/createtablewithparam?name=test&columns=column1,string;column2,double;column3,int")
                .then()
                .statusCode(200)
                .body(is("table "+table.getName()+" already exists"));
    }


}
