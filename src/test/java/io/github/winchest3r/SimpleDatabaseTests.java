package io.github.winchest3r;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.*;

/**
 * Class with simple h2 database connection tests.
 */
public class SimpleDatabaseTests {
    private static Connection conn;

    private final String REMOVE_TABLE_SQL = """
    DROP TABLE IF EXISTS dogs;
    """;

    private final String INIT_TEST_DB_SQL = """
    CREATE TABLE dogs (
        dogId INTEGER PRIMARY KEY AUTO_INCREMENT,
        dogName VARCHAR(40) NOT NULL
    );
    """;

    private final String INSERT_ONE_SQL = """
    INSERT INTO dogs (dogName)
    VALUES ('Bobik')
    """;

    private final String INSERT_FIVE_SQL = """
    INSERT INTO dogs (dogName)
    VALUES ('Bobik'), ('Max'), ('Rex'), ('Rocket'), ('Moon');    
    """;

    private final String GET_ALL_SQL = """
    SELECT * FROM dogs;        
    """;

    private final static String testDir = "./target/test-data/test";

    @BeforeAll
    static void databaseLoading() {
        //org.h2.Driver.load();
        assertDoesNotThrow(() -> {
            conn = DriverManager.getConnection("jdbc:h2:" + testDir, "sa", "");
        });
        assertNotNull(conn);
    }

    @BeforeEach
    void initializeDatabase() {
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
                assertEquals(addResult, 5);
                var result = statement.executeQuery(GET_ALL_SQL);
                while (result.next()) {
                    assertEquals(result.getString("dogName"), names.get(5 - addResult));
                    --addResult;
                }
                assertEquals(addResult, 0);
            }
        });
    }

    @AfterEach
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
