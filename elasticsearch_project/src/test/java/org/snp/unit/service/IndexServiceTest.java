package org.snp.unit.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.snp.dao.TableDao;
import org.snp.indexage.Table;
import org.snp.model.communication.Message;
import org.snp.model.communication.MessageAttachment;
import org.snp.model.credentials.ColumnCredentials;
import org.snp.model.credentials.IndexCredentials;
import org.snp.service.IndexService;
import org.snp.utils.TestFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class IndexServiceTest {

    @InjectMock
    TableDao tableDao;

    @Inject
    IndexService indexService;

    @Test
    public void testCreateHappyPath(){
        ArrayList<ColumnCredentials> list = new ArrayList<>();
        list.add(new ColumnCredentials("nom", "str"));
        IndexCredentials indexCredentials = new IndexCredentials("person",list);


        Mockito.when(tableDao.find("person")).thenReturn(Table
                                                                .builder()
                                                                .name("person")
                                                                .columns( TestFactory.createListColumn(List.of("nom","prenom","age")) )
                                                                .build());

        Message message = indexService.create(indexCredentials);

        Assertions.assertEquals(200, message.getCode());
        Assertions.assertTrue(message.hasAttachment());
        Table table = (Table) ((MessageAttachment)message).getAttachment();
        Assertions.assertEquals("person", table.getName());
        Assertions.assertEquals("nom", table.getColumns().get(0).getName() );
        Assertions.assertEquals("prenom", table.getColumns().get(1).getName() );
        Assertions.assertEquals("age", table.getColumns().get(2).getName() );
    }

    @Test
    public void testCreateTableDoesNotExist(){
        ArrayList<ColumnCredentials> list = new ArrayList<>();
        list.add(new ColumnCredentials("nom", "str"));
        IndexCredentials indexCredentials = new IndexCredentials("person",list);


        Mockito.when(tableDao.find("person")).thenReturn(null);

        Message message = indexService.create(indexCredentials);

        Assertions.assertEquals(404, message.getCode());
    }

    @Test
    public void testCreateIndexAlreadyExist(){
        ArrayList<ColumnCredentials> list = new ArrayList<>();
        list.add(new ColumnCredentials("nom", "str"));
        IndexCredentials indexCredentials = new IndexCredentials("person",list);


        Mockito.when(tableDao.find("person")).thenReturn(Table
                .builder()
                .name("person")
                .columns( TestFactory.createListColumn(List.of("nom","prenom","age")) )
                .build());

        indexService.create(indexCredentials);
        Message message = indexService.create(indexCredentials);

        Assertions.assertEquals(403, message.getCode());
    }
}
