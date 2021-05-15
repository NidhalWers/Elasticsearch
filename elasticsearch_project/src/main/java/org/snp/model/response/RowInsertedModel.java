package org.snp.model.response;

public class RowInsertedModel {
    public String table;
    public int position;

    public RowInsertedModel(){}

    public RowInsertedModel(String table, int position) {
        this.table = table;
        this.position = position;
    }
}
