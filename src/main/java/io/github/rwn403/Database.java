package io.github.rwn403;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Database {

    private static final String SCHEMA = "/DDL.sql"; 
    
    // Store the user's login credentials.
    private String url = "";
    private String username = "";
    private String password = "";

    private boolean loggedin;

    public Database() {
        // Load the JDBC PostgreSQL driver.
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Console.error("Failed to load the JDBC PostgreSQL driver.");
            System.exit(0);
        }
        loggedin = false;
    }

    /**
     * Set the user's login credentials
     * @param url The URL of the database.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the credentials are valid, false otherwise.
     */
    public boolean setCredentials(String url, String username, String password) {
        // Check credentials validity.
        try (Connection conn = DriverManager.getConnection(url, username, password);) {
            this.url = url;
            this.username = username;
            this.password = password;
            loggedin = true;
        } catch (SQLException e) {
            Console.exception(e);
            return false;
        }
        return true;
    }

    public boolean setUpDB() {
        if (!isLoggedIn()) return false;
        // Set up database schema.
        try (Connection conn = DriverManager.getConnection(url, username, password);) {
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
        } catch (SQLException | IOException e) {
            Console.exception(e);
            return false;
        }
        return true;
    }

    public boolean isLoggedIn() { return loggedin; }
}
