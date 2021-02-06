package org.snp.service;


import org.snp.indexage.entities.Column;
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
                        .name(credentials.getName())
                        .type(credentials.getType())
                        .build()
            );
        }

        return columns;
    }
}
