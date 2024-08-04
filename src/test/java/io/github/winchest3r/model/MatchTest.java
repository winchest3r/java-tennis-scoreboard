package io.github.winchest3r.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistenceMatchTest";

    /** Entity Manager for model testing. */
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void establishPersistentConnection() {
        sessionFactory = new Configuration()
            // H2
            .setProperty(AvailableSettings.JAKARTA_JDBC_URL, TEST_URL)
            // SQL statement logging
            .setProperty(AvailableSettings.SHOW_SQL, true)
            .setProperty(AvailableSettings.FORMAT_SQL, true)
            .setProperty(AvailableSettings.HIGHLIGHT_SQL, true)
            // Create a new SessionFactory
            .buildSessionFactory();
    }

    @Test
    void connectionIsEstablished() {
        assertNotNull(sessionFactory);
        assertTrue(sessionFactory.isOpen());
    }
}
