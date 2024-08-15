package io.github.winchest3r.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.List;

import io.github.winchest3r.model.*;
import io.github.winchest3r.util.TestingData;

public class PlaysetServiceTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistencePlaysetServiceTest";

    /** Entity Manager for model testing. */
    private static SessionFactory sessionFactory;

    /** Player service for testing. */
    private static PlaysetService playsetService;

    /**
     * Establish session before each tests.
     */
    @BeforeEach
    public void establishPersistentConnection() {
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

            playsetService = new PlaysetService(sessionFactory);
    }

    /** */
    @Test
    public void getPlaysetsByMatch() {
        var sampleMatch = TestingData.MATCHES.getFirst();
        sessionFactory.inSession(session -> {
            Match match = session.find(Match.class, sampleMatch.id());
            assertNotNull(match);

            List<Playset> playsets = playsetService.getPlaysetsByMatch(match);

            assertNotNull(playsets);
            assertEquals(TestingData.PLAYSETS
                .stream()
                .filter(p -> p.matchId().intValue() == sampleMatch.id())
                .count(),
            playsets.size());
        });
    }

    /** */
    @Test
    public void getPlaysetByUuid() {
        var samplePlayset = TestingData.PLAYSETS.get(1);
        sessionFactory.inSession(session -> {
            Playset playset = session.find(Playset.class, samplePlayset.id());
            assertNotNull(playset);

            Playset playsetFromUuid =
                playsetService.getPlaysetByUuid(playset.getUuid());
            assertNotNull(playsetFromUuid);

            assertEquals(playset, playsetFromUuid);
        });
    }

    /** */
    @Test
    public void getPlaysetByUnavailableUuid() {
        UUID uuid = UUID.randomUUID();
        sessionFactory.inSession(session -> {
            Playset playset = playsetService.getPlaysetByUuid(uuid);
            assertNull(playset);
        });
    }

    /** */
    @Test
    public void addNewPlayset() {
        var sampleMatch = TestingData.MATCHES.getLast();
        sessionFactory.inTransaction(session -> {
            Match match = session.find(Match.class, sampleMatch.id());
            assertNotNull(match);

            Playset playset = playsetService.addNewPlayset(match);

            assertNotNull(playset);
            assertNotNull(playset.getId());
            assertNotNull(playset.getUuid());
            assertNotNull(playset.getStartTime());
            assertNotNull(playset.getMatch());
            assertNotNull(playset.getMatch().getUuid());
        });
    }

    /** */
    @Test
    public void setPlayersSetScore() {
        var samplePlayset = TestingData.PLAYSETS.getLast();
        final int playerOneScore = 3;
        final int playerTwoScore = 5;
        sessionFactory.inTransaction(session -> {
            Playset playset = session.find(Playset.class, samplePlayset.id());
            assertNotNull(playset);

            playsetService.setPlayerOneSetScore(playset, playerOneScore);
            playsetService.setPlayerTwoSetScore(playset, playerTwoScore);
        });
        sessionFactory.inSession(session -> {
            Playset playset = session.find(Playset.class, samplePlayset.id());
            assertEquals(playerOneScore, playset.getPlayerOneSetScore());
            assertEquals(playerTwoScore, playset.getPlayerTwoSetScore());
        });
    }

    /** */
    @AfterEach
    public void closeSession() {
        if (sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
