package io.github.rwn403.database.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Store a SQL query to be executed.
 * @author RWN403
 * @version 1.0.1
 */
public class SQLQuery extends SQLStatement<List<List<Object>>> {

    /**
     * Create a SQL query object.
     * @param c The database connection to execute on.
     * @param statement The SQL statement.
     * @param args The arguments for the statement.
     * @throws SQLException
     */
    public SQLQuery(Connection c) throws SQLException {
        super(c);
    }
    
    /**
     * Execute an SQL query to retrieve matching results from the database.
     * @return The table of query results.
     */
    public List<List<Object>> execute() throws SQLException {
        ps = c.prepareStatement(statement);
        for (int i = 0; i < args.size(); i++) ps.setObject(i + 1, args.get(i));
        List<List<Object>> results = new ArrayList<>();
        // Convert result set into a table.
        ResultSet rs = ps.executeQuery();
        int numColumns = rs.getMetaData().getColumnCount();
        // Get the titles of the columns.
        List<Object> l = new ArrayList<>();
        for (int i = 0; i < numColumns; i++)
            l.add(rs.getMetaData().getColumnName(i + 1));
        results.add(l);
        // Get each row in the query results.
        while (rs.next()) {
            l = new ArrayList<>();
            for (int i = 0; i < numColumns; i++)
                l.add(rs.getObject(i + 1));
            results.add(l);
        }
        return results;
    }
}
