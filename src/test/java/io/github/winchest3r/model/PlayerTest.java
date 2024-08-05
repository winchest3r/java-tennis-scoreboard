package io.github.winchest3r.model;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import io.github.winchest3r.utils.TestingData;

public class PlayerTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistencePlayerTest";

    /** Entity Manager for model testing. */
    private static SessionFactory sessionFactory;

    /**
     * Establish session before each tests.
     */
    @BeforeEach
    public void establishPersistentConnection() {
        sessionFactory = new Configuration()
            .addAnnotatedClass(Player.class)
            .addAnnotatedClass(Match.class)
            .addAnnotatedClass(PlaySet.class)
            .addAnnotatedClass(Game.class)
            // H2
            .setProperty(AvailableSettings.JAKARTA_JDBC_URL, TEST_URL)
            // Credentials
            .setProperty(AvailableSettings.JAKARTA_JDBC_USER, "sa")
            // Automatic schema export
            .setProperty(
                AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                Action.SPEC_ACTION_DROP_AND_CREATE)
            // SQL statement logging
            .setProperty(AvailableSettings.SHOW_SQL, true)
            .setProperty(AvailableSettings.FORMAT_SQL, true)
            .setProperty(AvailableSettings.HIGHLIGHT_SQL, true)
            // Loading SQL script
            .setProperty(AvailableSettings.JAKARTA_HBM2DDL_LOAD_SCRIPT_SOURCE,
                "sql/tennis-test-dataset.sql")
            // Create a new SessionFactory
            .buildSessionFactory();
    }

    /** */
    @Test
    public void connectionIsEstablished() {
        try (Session session = sessionFactory.openSession()) {
            assertNotNull(session);
            assertTrue(session.isOpen());
        }
    }

    /** */
    @Test
    public void canGetPlayers() {
        try (Session session = sessionFactory.openSession()) {
            List<Player> players =
                session.createSelectionQuery("from Player", Player.class)
                    .getResultList();
            assertEquals(TestingData.PLAYERS.size(), players.size());
            for (Player p : players) {
                assertTrue(TestingData.PLAYERS.contains(p.getName()));
            }
        }
    }

    // TODO insert new player

    // TODO delete player

    // TODO update player

    /** */
    @AfterEach
    public void closeSession() {
        if (sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
