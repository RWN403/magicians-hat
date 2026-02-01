package io.github.rwn403.database;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.junit.jupiter.api.*;

import io.github.rwn403.Console;

/**
 * Test the database class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    private static final String CONFIG = "/config.properties";

    private Database db;

    /**
     * Wipe the test database and set up for unit tests.
     */
    @BeforeAll
    private void setup() {
        db = new Database();
        try (Reader in = new InputStreamReader(getClass().getResourceAsStream(CONFIG));) {
            Properties p = new Properties();
            p.load(in);
            String url = p.getProperty("db.url");
            String username = p.getProperty("db.username");
            String password = p.getProperty("db.password");
            if (!db.setCredentials(url, username, password))
                fail("Failed to connect to database.");
            if (!db.setup()) fail("Failed to set up database");
        } catch (IOException e) {
            Console.exception(e);
            fail("Failed to read database configuration file.");
        }
    }
}
