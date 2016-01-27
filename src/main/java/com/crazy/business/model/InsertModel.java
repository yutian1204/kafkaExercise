package com.crazy.business.model;

import java.util.List;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
public class InsertModel {

    private List<String> columns;

    private List<List<SqlValue>> rows;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<SqlValue>> getRows() {
        return rows;
    }

    public void setRows(List<List<SqlValue>> rows) {
        this.rows = rows;
    }
}
