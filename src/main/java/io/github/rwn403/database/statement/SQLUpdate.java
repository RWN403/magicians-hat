package io.github.rwn403.database.statement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Store a SQL update to be executed.
 * @author RWN403
 * @version 1.0.1
 */
public class SQLUpdate extends SQLStatement<Boolean> {

    /**
     * Create a SQL update object.
     * @param c The database connection to execute on.
     * @param statement The SQL statement.
     * @param args The arguments for the statement.
     * @throws SQLException
     */
    public SQLUpdate(Connection c) throws SQLException {
        super(c);
    }

    /**
     * Execute the SQL statement to update the database.
     * @return True if the database is updated successfully, false otherwise.
     */
    public Boolean execute() throws SQLException { 
        ps = c.prepareStatement(statement);
        for (int i = 0; i < args.size(); i++) ps.setObject(i + 1, args.get(i));
        ps.executeUpdate();
        return true;
    }
}
