package org.snp.service;


import org.snp.indexage.Column;
import org.snp.model.credentials.ColumnCredentials;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class ColumnService {

    public ArrayList<Column> getListColumns(ArrayList<ColumnCredentials> columnCredentials){
        ArrayList<Column> columns = new ArrayList<>();
        for(ColumnCredentials credentials : columnCredentials){
            columns.add(Column
                        .builder()
                        .name(credentials.columnName)
                        .type(credentials.type)
                        .build()
            );
        }

        return columns;
    }
}
