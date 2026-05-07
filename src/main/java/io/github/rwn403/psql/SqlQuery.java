package io.github.rwn403.psql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Store a SQL query to be executed.
 * @author RWN403
 * @version 1.0.0
 */
class SqlQuery extends SqlStatement<List<List<Object>>> {

    /**
     * Create a SQL query object.
     * @param c The database connection to execute on.
     * @param statement The SQL statement.
     * @param args The arguments for the statement.
     * @throws SQLException
     */
    SqlQuery(Connection c) throws SQLException {
        super(c);
    }
    
    /**
     * Execute an SQL query to retrieve matching results from the database.
     * @return The table of query results.
     * @throws SQLException
     */
    public SqlResult execute() throws SQLException {
        ps = c.prepareStatement(statement);
        for (int i = 0; i < args.size(); i++) ps.setObject(i + 1, args.get(i));
        try (ResultSet rs = ps.executeQuery()) {
            return new SqlResult(rs);
        }
    }
}
