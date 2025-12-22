package io.github.rwn403;

import java.sql.*;

public class Database {
    
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
            return false;
        }
        return true;
    }

    public boolean isLoggedIn() { return loggedin; }
}
