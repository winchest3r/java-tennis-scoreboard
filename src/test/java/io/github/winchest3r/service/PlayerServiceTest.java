package io.github.winchest3r.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import io.github.winchest3r.model.*;
import io.github.winchest3r.utils.TestingData;

public class PlayerServiceTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistencePlayerServiceTest";

    /** Entity Manager for model testing. */
    private static SessionFactory sessionFactory;

    /** Player service for testing. */
    private static PlayerService playerService;

    /**
     * Establish session before each tests.
     */
    @BeforeAll
    public static void establishPersistentConnection() {
        sessionFactory = new Configuration()
            .addAnnotatedClass(Player.class)
            .addAnnotatedClass(Match.class)
            .addAnnotatedClass(Playset.class)
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

        playerService = new PlayerService(sessionFactory);
    }

    /** */
    @Test
    public void getAllPlayers() {
        var players = playerService.getPlayers();
        assertEquals(TestingData.PLAYERS.size(), players.size());
        for (Player p : players) {
            assertTrue(TestingData.PLAYERS.stream().anyMatch(
                localP -> p.getName().equals(localP.name())
            ));
        }
    }

    /** */
    @Test
    public void getPlayerByUuid() {
        var samplePlayer = TestingData.PLAYERS.getFirst();
        // Need to find initialized sample player.
        sessionFactory.inSession(session -> {
            Player player = session.find(Player.class, samplePlayer.id());
            Player playerFromService = playerService
                .getPlayerByUuid(player.getUuid());
            assertNotNull(playerFromService);
        });
    }

    /** */
    @Test
    public void getPlayerByUnavailableUuid() {
        Player player = playerService.getPlayerByUuid(UUID.randomUUID());
        assertNull(player);
    }

    /** */
    @Test
    public void addNewPlayer() {
        final String name = "New Player";
        Player newPlayer = playerService.addNewPlayer(name);
        assertNotNull(newPlayer);
        assertEquals(name, newPlayer.getName());
        assertNotNull(newPlayer.getId());
        final String uuidPattern =
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        assertTrue(newPlayer
            .getUuid()
            .toString()
            .toLowerCase()
            .matches(uuidPattern)
        );
    }

    /** */
    @AfterAll
    public static void closeSession() {
        if (sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
