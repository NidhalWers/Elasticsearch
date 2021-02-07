package org.snp.unit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElasticsearchTest {
    static Table table;

    @BeforeAll
    public static void initTableToTest(){
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
    @Order(1)
    public void testCreateTableFirstTime(){


        given()
            .body("{\n" +
                    "    \"columns\" : [\n" +
                    "        {\"name\" : \"column1\", \"type\" : \"string\"},\n" +
                    "        {\"name\" : \"column2\", \"type\" : \"double\"},\n" +
                    "        {\"name\" : \"column3\", \"type\" : \"int\"}\n" +
                    "    ],\n" +
                    "    \"name\" : \"test\"\n" +
                    "}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/elasticsearch/createTable")
            .then()
            .statusCode(200)
            .body(is(table.toString()));
    }

    @Test
    @Order(2)
    public void testCreateTableSecondeTime(){
        given()
                .body("{\n" +
                        "    \"columns\" : [\n" +
                        "        {\"name\" : \"column1\", \"type\" : \"string\"},\n" +
                        "        {\"name\" : \"column2\", \"type\" : \"double\"},\n" +
                        "        {\"name\" : \"column3\", \"type\" : \"int\"}\n" +
                        "    ],\n" +
                        "    \"name\" : \"test\"\n" +
                        "}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/elasticsearch/createTable")
                .then()
                .statusCode(409)
                ;
    }



    @Test
    @Order(3)
    public void testAddIndexExistingTable(){
        table.addIndex("column1column2", Index.builder().build());
        given()
                .body("{\n" +
                        "    \"tableName\" : \""+table.getName()+"\",\n" +
                        "    \"columns\" : [\n" +
                        "        {\"name\" : \"column1\"},\n" +
                        "        {\"name\" : \"column2\"}\n" +
                        "    ] \n" +
                        "}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/elasticsearch/addIndex")
                .then()
                .statusCode(200)
                .body(is(table.toString()));
    }

    @Test
    @Order(4)
    public void testAddIndexNonExistingTable(){
        given()
                .body("{\n" +
                        "    \"tableName\" : \"bad test\",\n" +
                        "    \"columns\" : [\n" +
                        "        {\"name\" : \"column1\"},\n" +
                        "        {\"name\" : \"column2\"}\n" +
                        "    ] \n" +
                        "}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/elasticsearch/addIndex")
                .then()
                .statusCode(404)
                ;
    }


}
