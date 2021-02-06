package org.snp.utils;

import org.snp.model.Column;

import java.util.ArrayList;

public class FormatUtils {

    public static ArrayList<Column> getListColumns(String text){
        ArrayList<Column> columns = new ArrayList<>();
        String[] splitColumn = text.split(";");
        for(String columnText : splitColumn){
            String[] split = columnText.split(",");
            columns.add(Column.builder()
                    .name(split[0])
                    .type(split[1])
                    .build());
        }
        return columns;
    }
}
