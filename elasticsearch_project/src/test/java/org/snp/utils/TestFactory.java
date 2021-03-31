package org.snp.utils;

import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.indexage.entities.SubIndex;
import org.snp.service.TableService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFactory {



    static TableService tableService = new TableService();

    public static Table createTable(){
        Table table = Table
                .builder()
                .name("test")
                .columns( createListColumn(List.of("nom","prenom","age")) )
                .build();

        //index on nom
        List<Column> columns = createListColumn(List.of("nom"));
        tableService.addIndex(table, columns);

        //index on nom & prenom
        List<Column> columns2 = createListColumn(List.of("nom","prenom"));
        tableService.addIndex(table, columns2);

        List<Column> columns3 = createListColumn(List.of("nom","prenom","age"));
        tableService.addIndex(table, columns3);

        return table;

    }

    public static List<Column> createListColumn(List<String> names){
        List<Column> columns = new ArrayList<>();
        if(names.contains("nom")) {
            columns.add(Column.builder()
                    .type("string")
                    .name("nom")
                    .build());
        }
        if(names.contains("prenom")) {
            columns.add(Column.builder()
                    .name("prenom")
                    .type("string")
                    .build());
        }
        if(names.contains("age")) {
            columns.add(Column.builder()
                    .name("age")
                    .type("int")
                    .build());
        }

        return columns;
    }
}
