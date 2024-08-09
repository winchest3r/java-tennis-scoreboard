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
import io.github.winchest3r.utils.TestingData;

public class GameServiceTest {
    /**
     * Url to testing database.
     */
    private static final String TEST_URL =
        "jdbc:h2:./target/test-data/persistenceGameServiceTest";

    /** Entity Manager for model testing. */
    private static SessionFactory sessionFactory;

    /** Player service for testing. */
    private static GameService gameService;

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

        gameService = new GameService(sessionFactory);
    }

    /* TODO
     * Testing of:
     * getGameByPlayset
     * getGameByUuid
     * getGameByUnavailableUuid
     * addNewGame
     * setPlayerOneGameScore
     * setPlayerTwoGameScore
     */

    /** */
    @Test
    public void getGamesByPlayset() {
        var samplePlayset = TestingData.PLAYSETS.getLast();
        sessionFactory.inSession(session -> {
            Playset playset = session.find(Playset.class, samplePlayset.id());
            assertNotNull(playset);

            List<Game> games = gameService.getGamesByPlayset(playset);
            assertNotNull(games);
            assertEquals(
                TestingData.GAMES
                    .stream()
                    .filter(g -> g.playsetId().intValue() == playset.getId())
                    .count(),
                games.size());
        });
    }

    /** */
    @Test
    public void getGameByUuid() {
        var sampleGame = TestingData.GAMES.getLast();
        sessionFactory.inSession(session -> {
            Game game = session.find(Game.class, sampleGame.id());
            assertNotNull(game);

            Game gameFromUuid = gameService.getGameByUuid(game.getUuid());
            assertNotNull(gameFromUuid);
            assertEquals(game, gameFromUuid);
        });
    }

    /** */
    @Test
    public void getGamesByUnavailableUuid() {
        UUID uuid = UUID.randomUUID();
        sessionFactory.inSession(session -> {
            Game game = gameService.getGameByUuid(uuid);
            assertNull(game);
        });
    }

    /** */
    @Test
    public void addNewGame() {
        var samplePlayset = TestingData.PLAYSETS.getLast();
        sessionFactory.inTransaction(session -> {
            Playset playset = session.find(Playset.class, samplePlayset.id());
            assertNotNull(playset);

            Game game = gameService.addNewGame(playset);

            assertNotNull(game);
            assertNotNull(game.getId());
            assertNotNull(game.getUuid());
            assertNotNull(game.getStartTime());
            assertNotNull(game.getPlayset());
            assertNotNull(game.getPlayset().getMatch());
        });
    }

    /** */
    @Test
    public void setPlayersGameScore() {
        var sampleGame = TestingData.GAMES.getLast();
        final int playerOneScore = 4;
        final int playerTwoScore = 2;
        sessionFactory.inTransaction(session -> {
            Game game = session.find(Game.class, sampleGame.id());
            assertNotNull(game);

            gameService.setPlayerOneGameScore(game, playerOneScore);
            gameService.setPlayerTwoGameScore(game, playerTwoScore);
        });
        sessionFactory.inSession(session -> {
            Game game = session.find(Game.class, sampleGame.id());
            assertEquals(playerOneScore, game.getPlayerOneGameScore());
            assertEquals(playerTwoScore, game.getPlayerTwoGameScore());
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
