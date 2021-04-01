package org.snp.unit.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.indexage.Column;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.service.ColumnService;

import javax.inject.Inject;
import java.util.ArrayList;

@QuarkusTest
public class ColumnServiceTest {

    @Inject
    ColumnService columnService;

    @Test
    public void testGetListColumns(){
        ArrayList<ColumnCredentials> list = new ArrayList<>();
        list.add(new ColumnCredentials("nom", "str"));
        list.add(new ColumnCredentials("prenom", "str"));
        list.add(new ColumnCredentials("age", "int"));

        ArrayList<Column> columns = columnService.getListColumns(list);

        Assertions.assertEquals("nom", columns.get(0).getName());
        Assertions.assertEquals("str", columns.get(0).getType());

        Assertions.assertEquals("prenom", columns.get(1).getName());
        Assertions.assertEquals("str", columns.get(1).getType());

        Assertions.assertEquals("age", columns.get(2).getName());
        Assertions.assertEquals("int", columns.get(2).getType());
    }

}
