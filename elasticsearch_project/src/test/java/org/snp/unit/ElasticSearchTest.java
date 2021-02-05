package test.java.org.snp.unit;

import io.quarkus.test.junit.QuarkusTest;
import main.java.org.snp.model.Column;
import main.java.org.snp.model.Table;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class ElasticSearchTest {

    @Test
    public void testCreateTable(){
        Table table = Table
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

        given()
            .when().get("/elasticsearch/createtable?name=test&columns=column1,string;column2,double;column3,int")
            .then()
            .statusCode(200)
            .body(is(table.toString()));
    }
}
