package io.github.rwn403;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Handle interactions with a PostgreSQL database.
 * @author RWN403
 * @version 1.0
 */
public class Database {

    private static final String SCHEMA = "/DDL.sql"; 
    
    // Store the user's login credentials.
    private String url;
    private String username;
    private String password;

    private boolean isConnected;

    public Database() {
        // Load the JDBC PostgreSQL driver.
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Console.error("Failed to load the JDBC PostgreSQL driver.");
            System.exit(0);
        }
        url = "";
        username = "";
        password = "";
        isConnected = false;
    }

    /**
     * Connect to the database.
     * @param url The URL of the database.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if database is successfully connected, false otherwise.
     */
    public boolean connect(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        // Check credentials validity.
        validateConnection();
        return isConnected;
    }

    /**
     * Set up the database according to the schema.
     * @return True if the database is successfully set up, false otherwise.
     */
    public boolean setup() {
        if (!isConnected) return false;
        // Set up database schema.
        try (Connection conn = getConnection();) {
            Statement s = conn.createStatement();
            // Read the SQL schema file.
            BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(SCHEMA))
            );
            // Execute each DDL command.
            String query = "";
            String line;
            while((line = br.readLine()) != null) {
                query += line + " ";
                if (line.trim().endsWith(";")) {
                    s.execute(query);
                    query = "";
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        } catch (IOException e) {
            Console.exception(e);
            return false;
        }
        return true;
    }

    /**
     * Execute an SQL query to retrieve matching results from the database.
     * @param query The SQL query.
     * @param args The arguments for the query.
     * @return The table of query results.
     */
    public List<List<Object>> executeQuery(String query, Object... args) {
        if (!isConnected) return null;
        List<List<Object>> results = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(query);) {
            for (int i = 0; i < args.length; i++)
                ps.setObject(i + 1, args[i]);
            try (ResultSet rs = ps.executeQuery();) {
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
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return results;
    }

    /**
     * Execute an SQL statement to update the database.
     * @param statement The SQL statement.
     * @param args The arguments for the statement.
     * @return True if the statement is successfully executed, false otherwise.
     */
    public boolean executeUpdate(String statement, Object... args) {
        if (!isConnected) return false;
        try (PreparedStatement ps = getConnection().prepareStatement(statement);) {
            for (int i = 0; i < args.length; i++)
                ps.setObject(i + 1, args[i]);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    /**
     * Get a connection to the database.
     * @return A connection to the database.
     */
    private Connection getConnection() {
        if (!isConnected) return null;
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    /**
     * Update the connection status of the database.
     */
    private void validateConnection() {
        isConnected = true;
        try (Connection conn = getConnection();) {
            if (conn != null) isConnected = true;
            Console.database("Database connection established successfully.");
        } catch (SQLException e) {
            Console.exception(e);
            Console.database("Failed to establish database connection.");
            isConnected = false;
        }
    }

    /**
     * Handle exceptions resulting from SQL.
     * @param e The SQL exception to be handled.
     */
    private void handleSQLException(SQLException e) {
        Console.exception(e);
        Console.database("Attempting to re-establish database connection...");
        validateConnection();
    }
}
