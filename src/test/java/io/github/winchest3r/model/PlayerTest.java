package io.github.winchest3r.model;

import jakarta.persistence.*;
import org.hibernate.cfg.AvailableSettings;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class PlayerTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistencePlayerTest";

    /** Entity Manager for model testing. */
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void establishPersistentConnection() {
        entityManagerFactory =
        Persistence.createEntityManagerFactory(
            "io.github.winchest3r",
            Map.of(AvailableSettings.JAKARTA_JDBC_URL, TEST_URL)
        );
    }

    @Test
    void connectionIsEstablished() {
        assertNotNull(entityManagerFactory);
        assertTrue(entityManagerFactory.isOpen());
    }
}
