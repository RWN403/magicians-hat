package io.github.rwn403.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.rwn403.Console;
import io.github.rwn403.database.statement.*;

/**
 * Handle interactions with a PostgreSQL database.
 * @author RWN403
 * @version 2.0.1
 */
public class Database {

    private static final String SCHEMA = "/DDL.sql"; 
    
    // Store the user's login credentials.
    private String url;
    private String username;
    private String password;

    public Database() {
        Console.database("Intializing database...");
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
        Console.database("Database initialized.");
    }

    /**
     * Set the login credentials of the database.
     * @param url The URL of the database.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the credentials are valid, false otherwise.
     */
    public boolean setCredentials(String url, String username, String password) {
        Console.database("Connecting to database...");
        this.url = url;
        this.username = username;
        this.password = password;
        // Check credentials validity.
        try (
            Connection conn = getConnection();
        ) {
            return conn.isValid(0);
        } catch (SQLException e) {
            Console.error("Failed to validate database login credentials.");
            Console.exception(e);
            return false;
        }
    }

    /**
     * Set up the database according to the schema.
     * @return True if the database is successfully set up, false otherwise.
     */
    public boolean setup() {
        Console.database("Setting up database...");
        // Set up database schema.
        try (
            Connection conn = getConnection();
            SQLUpdate s = new SQLUpdate(conn);
        ) {
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
                    s.setStatement(query);
                    s.execute();
                    query = "";
                }
            }
        } catch (SQLException e) {
            Console.error("Failed to set up database.");
            Console.exception(e);
            return false;
        } catch (IOException e) {
            Console.error("Failed to read schema file.");
            Console.exception(e);
            return false;
        }
        Console.database("Database set up successfully.");
        return true;
    }

    /**
     * Get a connection to the database.
     * @return A connection to the database.
     */
    private Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            Console.error("Failed to retrieve database connection.");
            return null;
        }
    }
}
