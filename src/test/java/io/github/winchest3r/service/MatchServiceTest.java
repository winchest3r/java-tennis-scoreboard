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

public class MatchServiceTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistenceMatchServiceTest";

    /** Entity Manager for model testing. */
    private static SessionFactory sessionFactory;

    /** Player service for testing. */
    private static MatchService matchService;

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

        matchService = new MatchService(sessionFactory);
    }

    /** */
    @Test
    public void getMatches() {
        sessionFactory.inSession(session -> {
            List<Match> matches = matchService.getMatches();
            assertNotNull(matches);
            assertEquals(TestingData.MATCHES.size(), matches.size());
            for (Match m : matches) {
                assertTrue(TestingData.MATCHES.stream().anyMatch(sampleM ->
                    sampleM.playerOneId()
                        == m.getPlayerOne().getId().longValue()
                    && sampleM.playerTwoId()
                        == m.getPlayerTwo().getId().longValue()
                ));
            }
        });
    }

    /** */
    @Test
    public void getMatchByUuid() {
        var sampleMatch = TestingData.MATCHES.getLast();
        sessionFactory.inSession(session -> {
            Match match = session.find(Match.class, sampleMatch.id());
            assertNotNull(match);

            Match matchByUuid = matchService.getMatchByUuid(match.getUuid());
            assertNotNull(matchByUuid);
            assertEquals(match, matchByUuid);
        });
    }

    /** */
    @Test
    public void getMatchByUnavailableUuid() {
        final UUID uuid = UUID.randomUUID();
        sessionFactory.inSession(session -> {
            Match matchByUuid = matchService.getMatchByUuid(uuid);
            assertNull(matchByUuid);
        });
    }

    /** */
    @Test
    public void getMatchesByPlayer() {
        var samplePlayer = TestingData.PLAYERS.get(1);
        sessionFactory.inSession(session -> {
            Player player = session.find(Player.class, samplePlayer.id());
            List<Match> matches = matchService.getMatchesByPlayer(player);
            assertNotNull(matches);
            assertEquals(
                TestingData.MATCHES
                    .stream()
                    .filter(m -> m.playerOneId() == samplePlayer.id()
                        || m.playerTwoId() == samplePlayer.id())
                    .count(),
                matches.size()
            );
        });
    }

    /** */
    @Test
    public void getMatchesByNullPlayerWithException() {
        sessionFactory.inSession(session -> {
            assertThrows(NullPointerException.class, () -> {
                matchService.getMatchesByPlayer(null);
            });
        });
    }

    /** */
    @Test
    public void addNewMatch() {
        var p1 = TestingData.PLAYERS.getFirst();
        var p2 = TestingData.PLAYERS.getLast();
        sessionFactory.inTransaction(session -> {
            Player playerOne = session.find(Player.class, p1.id());
            assertNotNull(playerOne);
            Player playerTwo = session.find(Player.class, p2.id());
            assertNotNull(playerTwo);

            Match match = matchService.addNewMatch(playerOne, playerTwo);
            assertNotNull(match);

            assertEquals(playerOne, match.getPlayerOne());
            assertEquals(playerTwo, match.getPlayerTwo());
            assertNotNull(match.getId());
            assertNotNull(match.getUuid());
            assertNull(match.getWinner());
        });
    }

    /** */
    @Test
    public void setMatchWinner() {
        var sampleMatch = TestingData.MATCHES.getLast();
        sessionFactory.inSession(session -> {
            Match match = session.find(Match.class, sampleMatch.id());

            assertNotNull(match);
            assertNotNull(match.getPlayerOne());
            assertNotNull(match.getPlayerTwo());
            assertNull(match.getWinner());

            matchService.setMatchWinner(match, match.getPlayerOne());
            assertNotNull(match.getWinner());
            assertEquals(match.getPlayerOne(), match.getWinner());
        });
    }

    /** */
    @Test
    public void setWrongMatchWinner() {
        var sampleMatch = TestingData.MATCHES.getLast();
        var samplePlayer = TestingData.PLAYERS.getFirst();
        sessionFactory.inSession(session -> {
            Match match = session.find(Match.class, sampleMatch.id());
            Player player = session.find(Player.class, samplePlayer.id());

            assertNotNull(match);
            assertNull(match.getWinner());

            assertNotEquals(player, match.getPlayerOne());
            assertNotEquals(player, match.getPlayerTwo());

            assertThrows(IllegalArgumentException.class, () -> {
                matchService.setMatchWinner(match, player);
            });
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
