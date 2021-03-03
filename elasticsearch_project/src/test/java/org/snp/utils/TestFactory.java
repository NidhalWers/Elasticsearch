package org.snp.utils;

import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.indexage.helpers.SubIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFactory {

    public static Index createIndexNom(){
        Index index;

        Map<String, SubIndex> subIndexMap = new HashMap<>();
        subIndexMap.put("nom", SubIndex.builder()
                                        .column(Column.builder()
                                                .name("nom")
                                                .type("String")
                                                .build())
                                        .build());

        ArrayList<Column> column = new ArrayList<>();
        column.add(Column.builder()
                        .name("nom")
                        .type("String")
                        .build());
        //créer la map
        index = Index.builder()
                .columns(column)
                .subIndexes(subIndexMap)
                .build();

        return index;
    }

    public static Table createTable(){
        Table table = Table
                .builder()
                .name("test")
                .columns( createListColumn(List.of("nom","prenom","age")) )
                .build();

        /*
        table.addColumn(Column.builder()
                .type("string")
                .name("nom")
                .build());
        table.addColumn(Column.builder()
                .name("prenom")
                .type("string")
                .build());
        table.addColumn(Column.builder()
                .name("age")
                .type("int")
                .build());
         */

        //index on nom
        List<Column> columns = createListColumn(List.of("nom"));/*new ArrayList<>();
        columns.add(Column.builder()
                .name("nom")
                .type("string")
                .build());*/

        table.createIndex(columns);
        //index on nom & prenom
        List<Column> columns2 = createListColumn(List.of("nom","prenom"));/*new ArrayList<>();
        columns2.add(Column.builder()
                .name("nom")
                .type("string")
                .build());
        columns2.add(Column.builder()
                .name("prenom")
                .type("string")
                .build());*/

        table.createIndex(columns2);

        List<Column> columns3 = createListColumn(List.of("nom","prenom","age"));/*new ArrayList<>();
        columns3.add(Column.builder()
                .name("nom")
                .type("string")
                .build());
        columns3.add(Column.builder()
                .name("prenom")
                .type("string")
                .build());
        columns3.add(Column.builder()
                .name("age")
                .type("int")
                .build());*/

        table.createIndex(columns3);

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
