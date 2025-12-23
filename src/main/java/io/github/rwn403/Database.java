package io.github.rwn403;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

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
     * @return True if the database was successfully set up, false otherwise.
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
