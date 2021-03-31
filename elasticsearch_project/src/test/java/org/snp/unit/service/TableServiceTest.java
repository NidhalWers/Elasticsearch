package org.snp.unit.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.model.credentials.TableCredentials;
import org.snp.service.ColumnService;
import org.snp.service.TableService;
import org.snp.utils.TestFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class TableServiceTest {

    @Inject
    TableService tableService;

    @Inject
    ColumnService columnService;

    @Test
    public void testCreateOneTime(){
        ArrayList<ColumnCredentials> list = new ArrayList<>();
        list.add(new ColumnCredentials("nom", "str"));
        list.add(new ColumnCredentials("prenom", "str"));
        list.add(new ColumnCredentials("age", "int"));

        TableCredentials tableCredentials = new TableCredentials("person", list);
        ArrayList<Column> columns = columnService.getListColumns(tableCredentials.getColumns());
        Table table = Table
                .builder()
                .name(tableCredentials.getName())
                .columns(columns)
                .build();

        Message message = tableService.create(tableCredentials);

        Assertions.assertEquals(200, message.getCode());
        Assertions.assertEquals(table, ((MessageAttachment)message).getAttachment());
    }

    @Test
    public void testCreateSameTwoTime(){
        ArrayList<ColumnCredentials> list = new ArrayList<>();
        list.add(new ColumnCredentials("nom", "str"));
        list.add(new ColumnCredentials("prenom", "str"));
        list.add(new ColumnCredentials("age", "int"));

        TableCredentials tableCredentials = new TableCredentials("person", list);


        tableService.create(tableCredentials);

        Message message = tableService.create(tableCredentials);


        Assertions.assertEquals(403, message.getCode());
     }

     @Test
    public void testAddIndex(){
        Table table = Table
                 .builder()
                 .name("test")
                 .columns( TestFactory.createListColumn(List.of("nom","prenom","age")) )
                 .build();
        List<String> names = Arrays.asList(new String[]{"nom"});
        List<Column> cols = TestFactory.createListColumn(names);

        boolean boo = tableService.addIndex(table, cols);

        Assertions.assertTrue(boo);
        Assertions.assertTrue(table.getIndexes().keySet().contains("nom"));
     }



















}
