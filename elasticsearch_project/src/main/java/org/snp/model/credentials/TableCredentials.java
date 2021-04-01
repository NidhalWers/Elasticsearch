package org.snp.model.credentials;




import java.util.ArrayList;

public class TableCredentials {

    public String name;

    public ArrayList<ColumnCredentials> columns;

    public TableCredentials(){

    }


    public TableCredentials(String name, ArrayList<ColumnCredentials> columns) {
        this.name = name;
        this.columns = columns;
    }
}
