package org.snp.integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.snp.indexage.Column;
import org.snp.indexage.Table;
import org.snp.service.TableService;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TableAndIndexTest {
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
                .post("/table/")
                .then()
                .statusCode(200)
                .body(
                        "size()", is(4),
                        "columns", iterableWithSize(3),
                        "indexes", is(new TreeMap<>()),
                        "name", is("test"),
                        "dictionnaireMap", is(new HashMap<>())
                );
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
                .post("/table/")
                .then()
                .statusCode(409)
                .body(is("table test already exists"))
        ;
    }

    @Inject
    TableService tableService;

    @Test
    @Order(3)
    public void testAddIndexExistingTable(){
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(Column.builder()
                .name("column1")
                .type("string")
                .build());

        columns.add(Column.builder()
                .name("column2")
                .type("string")
                .build());

        tableService.addIndex(table, columns);

        given()
                .body("{\n" +
                        "    \"table_name\" : \""+table.getName()+"\",\n" +
                        "    \"columns\" : [\n" +
                        "        {\"name\" : \"column1\"},\n" +
                        "        {\"name\" : \"column2\"}\n" +
                        "    ] \n" +
                        "}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/index/add")
                .then()
                .statusCode(200)
                .body(
                        "size()", is(4),
                        "columns", iterableWithSize(3),
                        //"indexes", is(T),
                        "name", is("test")
                        //"subIndexMap", hasSize(2)
                );
    }
    @Test
    @Order(4)
    public void testAddIndexNonExistingTable(){
        given()
                .body("{\n" +
                        "    \"table_name\" : \"bad test\",\n" +
                        "    \"columns\" : [\n" +
                        "        {\"name\" : \"column1\"},\n" +
                        "        {\"name\" : \"column2\"}\n" +
                        "    ] \n" +
                        "}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/index/add")
                .then()
                .statusCode(404)
                .body(is("table bad test does not exist"))
        ;
    }


}
