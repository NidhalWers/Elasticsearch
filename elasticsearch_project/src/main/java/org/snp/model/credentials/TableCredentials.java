package org.snp.model.credentials;

import java.util.ArrayList;

public class TableCredentials {
    private String name;
    private ArrayList<ColumnCredentials> columnCredentialsArrayList;

    public TableCredentials(){

    }

    public TableCredentials(String name, ArrayList<ColumnCredentials> columnCredentialsArrayList) {
        this.name = name;
        this.columnCredentialsArrayList = columnCredentialsArrayList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ColumnCredentials> getColumnCredentialsArrayList() {
        return columnCredentialsArrayList;
    }
}
