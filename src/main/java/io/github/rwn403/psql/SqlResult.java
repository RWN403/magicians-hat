package io.github.rwn403.psql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represent a result from an SQL query.
 * @author RWN403
 * @version 1.0.0
 */
class SqlResult implements Iterable<Object[]> {

    private int numColumns;
    private int numRows;
    
    private String[] headers;
    private List<Object[]> rows;

    /**
     * Convert a result set into an SQL result object.
     * @param rs The result set to convert.
     * @throws SQLException
     */
    SqlResult(ResultSet rs) throws SQLException {
        numColumns = rs.getMetaData().getColumnCount();
        headers = new String[numColumns];
        // Get the titles of the columns.
        for (int i = 0; i < numColumns; i++)
            headers[i] = rs.getMetaData().getColumnName(i + 1);
        // Get each row in the query results.
        rows = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[numColumns];
            for (int i = 0; i < numColumns; i++)
                row[i] = rs.getObject(i + 1);
            rows.add(row);
        }
        numRows = rows.size();
    }

    public int getNumColumns() { return numColumns; }
    public int getNumRows() { return numRows; }
    public String getColumnName(int columnNum) { return headers[columnNum]; }
    public Object[] getRow(int rowNum) { return rows.get(rowNum); }

    /**
     * Extract the generated ID of a row inserted into a database.
     * @return The ID of the inserted row.
     */
    public Integer extractInsertId() {
        try {
            for (Object[] row: this) return ((Integer) row[0]);
            return null;
        } catch (Exception e) { return null; }
    }

    /**
     * Extract the IDs matching a database search.
     * @return The IDs matching the database search.
     */
    public List<Integer> extractMatchingIds() {
        List<Integer> ids = new ArrayList<>();
        try {
            for (Object[] row: this) ids.add((Integer) row[0]);
        } catch (Exception e) {}
        return ids;
    }

    @Override
    public Iterator<Object[]> iterator() {
        return new SQLResultIterator(this);
    }
}

class SQLResultIterator implements Iterator<Object[]> {

    private SqlResult result;
    private int i;

    SQLResultIterator(SqlResult result) {
        this.result = result;
        i = -1;
    }

    @Override
    public boolean hasNext() {
        return i < result.getNumRows() - 1;
    }

    @Override
    public Object[] next() {
        i++;
        return result.getRow(i);
    }
}
