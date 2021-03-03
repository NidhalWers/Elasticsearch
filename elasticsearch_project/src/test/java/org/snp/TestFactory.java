package org.snp;

import org.snp.indexage.entities.Column;
import org.snp.indexage.entities.Index;
import org.snp.indexage.entities.Table;
import org.snp.indexage.helpers.SubIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestFactory {

    public static Index createIndexNom(){
        Index index;

        Map<String, SubIndex> indexMap = new HashMap<>();
        indexMap.put("name", SubIndex.builder()
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
                .subIndexes(indexMap)
                .build();

        return index;
    }

    public static Table createTable() throws Exception {
        Table table = Table
                .builder()
                .name("test")
                .build();

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

        //index on nom
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(Column.builder()
                .name("nom")
                .type("string")
                .build());

        table.createIndex(columns);
        //index on nom & prenom
        ArrayList<Column> columns2 = new ArrayList<>();
        columns2.add(Column.builder()
                .name("nom")
                .type("string")
                .build());
        columns2.add(Column.builder()
                .name("prenom")
                .type("string")
                .build());

        table.createIndex(columns2);

        ArrayList<Column> columns3 = new ArrayList<>();
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
                .build());

        table.createIndex(columns3);

        return table;

    }
}
