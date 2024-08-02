package io.github.winchest3r;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.*;

/**
 * Class with simple h2 database connection tests.
 */
public class SimpleDatabaseTests {
    /**
     * Connection to h2 database.
     */
    private static Connection conn;

    /**
     * Query to remove table from database.
     */
    private static final String REMOVE_TABLE_SQL = """
    DROP TABLE IF EXISTS dogs;
    """;

    /**
     * Query to create simple test table in database.
     */
    private static final String INIT_TEST_DB_SQL = """
    CREATE TABLE dogs (
        dogId INTEGER PRIMARY KEY AUTO_INCREMENT,
        dogName VARCHAR(40) NOT NULL
    );
    """;

    /**
     * Query to insert one object in table.
     */
    private static final String INSERT_ONE_SQL = """
    INSERT INTO dogs (dogName)
    VALUES ('Bobik')
    """;

    /**
     * Query to insert five objects in table.
     */
    private static final String INSERT_FIVE_SQL = """
    INSERT INTO dogs (dogName)
    VALUES ('Bobik'), ('Max'), ('Rex'), ('Rocket'), ('Moon');
    """;

    /**
     * Query to select all data from test table.
     */
    private static final String GET_ALL_SQL = """
    SELECT * FROM dogs;
    """;

    /**
     * Path to database.
     */
    private static final String TEST_DIR = "./target/test-data/test";

    @BeforeAll
    static void databaseLoading() {
        // org.h2.Driver.load();
        assertDoesNotThrow(() -> {
            conn = DriverManager.getConnection("jdbc:h2:" + TEST_DIR, "sa", "");
        });
        assertNotNull(conn);
    }

    @BeforeEach
    final void initializeDatabase() {
        assertNotNull(conn);
        assertDoesNotThrow(() -> {
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate(REMOVE_TABLE_SQL);
                statement.executeUpdate(INIT_TEST_DB_SQL);
            }
        });
    }

    @Test
    void addOne() {
        assertDoesNotThrow(() -> {
            try (Statement statement = conn.createStatement()) {
                int addResult = statement.executeUpdate(INSERT_ONE_SQL);
                assertEquals(addResult, 1);
                var result = statement.executeQuery(GET_ALL_SQL);
                while (result.next()) {
                    --addResult;
                    assertEquals(result.getString("dogName"), "Bobik");
                }
                assertEquals(addResult, 0);
            }
        });
    }

    @Test
    void addFive() {
        List<String> names = List.of("Bobik", "Max", "Rex", "Rocket", "Moon");
        assertDoesNotThrow(() -> {
            try (Statement statement = conn.createStatement()) {
                int addResult = statement.executeUpdate(INSERT_FIVE_SQL);
                assertEquals(addResult, names.size());
                var result = statement.executeQuery(GET_ALL_SQL);
                while (result.next()) {
                    assertEquals(result.getString(
                        "dogName"), names.get(names.size() - addResult)
                );
                    --addResult;
                }
                assertEquals(addResult, 0);
            }
        });
    }

    @AfterEach
    final
    void cleanDatabase() {
        assertNotNull(conn);
        assertDoesNotThrow(() -> {
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate(REMOVE_TABLE_SQL);
            }
        });
    }

    @AfterAll
    static void close() {
        assertDoesNotThrow(() -> {
            conn.close();
        });
    }
}
