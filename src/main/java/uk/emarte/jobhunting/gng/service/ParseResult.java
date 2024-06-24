package uk.emarte.jobhunting.gng.service;

import java.util.List;

public class ParseResult {
    private final List<String> headers;
    private final List<String[]> rows;

    public ParseResult(List<String> headers, List<String[]> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public int getRowCount() {
        return rows.size();
    }

    public String getValue(int row, String key) {
        if (headers.contains(key)) {
            return rows.get(row)[headers.indexOf(key)];
        }

        return null;
    }
}
