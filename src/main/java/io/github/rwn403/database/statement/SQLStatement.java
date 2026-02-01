package io.github.rwn403.database.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a SQL statement to be executed.
 * @author RWN403
 * @version 1.0.1
 */
abstract class SQLStatement<T> implements AutoCloseable {

    protected Connection c;
    protected PreparedStatement ps;
    protected String statement;
    protected List<Object> args;

    /**
     * Create a SQL statement object.
     * @param c The database connection to execute on.
     * @param statement The SQL statement.
     * @param args The arguments for the statement.
     * @throws SQLException
     */
    public SQLStatement(Connection c) throws SQLException {
        this.c = c;
        statement = "";
        args = new ArrayList<>();
    }

    public void setStatement(String statement) { this.statement = statement; }

    public void setArgs(Object... args) {
        for (int i = 0; i < args.length; i++) this.args.add(args[i]);
    }

    // Set the statement and arguments in the prepared statement.
    // Declare how the SQL statement should be executed.
    public abstract T execute() throws SQLException;

    public void close() throws SQLException { ps.close(); }
}
