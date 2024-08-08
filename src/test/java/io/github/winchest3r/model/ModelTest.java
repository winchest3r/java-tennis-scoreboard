package io.github.winchest3r.model;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.MutationQuery;
import org.hibernate.tool.schema.Action;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.random.RandomGenerator;
import java.util.List;
import java.util.UUID;

import io.github.winchest3r.utils.TestingData;

/**
 * Cases for direct model testing without
 * service through Hibernate's SessionFactory.
 */
public class ModelTest {
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
    }

    /** */
    @Test
    public void establishConnection() {
        try (Session session = sessionFactory.openSession()) {
            assertNotNull(session);
            assertTrue(session.isOpen());
        }
    }

    /** */
    @Test
    public void getExistingPlayers() {
        try (Session session = sessionFactory.openSession()) {
            SelectionQuery<Player> query =
                session.createSelectionQuery("from Player", Player.class);
            assertEquals(TestingData.PLAYERS.size(), query.getResultCount());
            for (Player p : query.getResultList()) {
                assertTrue(TestingData.PLAYERS
                    .stream()
                    .anyMatch(
                        testPlayer -> p.getName().equals(testPlayer.name())
                    ));
            }
        }
    }

    /** */
    @Test
    public void getOneExistingPlayer() {
        try (Session session = sessionFactory.openSession()) {
            int randIdx =
                RandomGenerator
                    .getDefault()
                    .nextInt(TestingData.PLAYERS.size());
            var samplePlayerName = TestingData.PLAYERS.get(randIdx);

            Player player = session
                .createSelectionQuery(
                    "from Player where name = ?1",
                    Player.class)
                .setParameter(1, samplePlayerName.name())
                .getSingleResultOrNull();

            assertNotNull(player);
            assertEquals(player.getName(), samplePlayerName.name());
        }
    }

    /** */
    @Test
    public void insertNewPlayer() {
        String playerName = "Player Four";
        try (Session session = sessionFactory.openSession()) {
            var tx = session.getTransaction();
            try {
                tx.begin();
                MutationQuery query = session.createMutationQuery(
                    "insert Player (id, name) values (42, :playerName)"
                );
                query.setParameter("playerName", playerName);
                int res = query.executeUpdate();
                tx.commit();
                assertEquals(res, 1);
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
            SelectionQuery<Player> query = session
                .createSelectionQuery(
                    "from Player where name = ?1",
                    Player.class)
                .setParameter(1, playerName);
            Player newPlayer = query.getSingleResultOrNull();

            assertNotNull(newPlayer);
            assertNotNull(newPlayer.getId());
            assertNotNull(newPlayer.getUuid());
            assertNotNull(newPlayer.getMatchesWon());

            assertEquals(playerName, newPlayer.getName());
        }
    }

    /** */
    @Test
    public void deleteExistingPlayer() {
        var playerName = TestingData.PLAYERS.get(1);
        try (Session session = sessionFactory.openSession()) {
            var tx = session.getTransaction();
            try {
                tx.begin();
                MutationQuery query = session.createMutationQuery(
                    "delete Player where name = ?1"
                );
                query.setParameter(1, playerName.name());
                int res = query.executeUpdate();
                tx.commit();
                assertEquals(res, 1);
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
            SelectionQuery<Player> query = session
                .createSelectionQuery(
                    "from Player where name = ?1",
                    Player.class)
                .setParameter(1, playerName.name());
            Player newPlayer = query.getSingleResultOrNull();

            assertNull(newPlayer);
        }
    }

    /** */
    @Test
    public void updateExistingPlayer() {
        var playerName = TestingData.PLAYERS.get(2);
        String newName = "Super Player";
        try (Session session = sessionFactory.openSession()) {
            var tx = session.getTransaction();
            try {
                tx.begin();
                Player player = session
                    .createSelectionQuery(
                        "from Player where name = ?1",
                        Player.class)
                    .setParameter(1, playerName.name())
                    .getSingleResult();
                player.setName(newName);
                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
            SelectionQuery<Player> query = session
                .createSelectionQuery(
                    "from Player where name = ?1",
                    Player.class)
                .setParameter(1, newName);
            Player newNamePlayer = query.getSingleResultOrNull();

            assertNotNull(newNamePlayer);
            assertEquals(newName, newNamePlayer.getName());
        }
    }

    /** */
    @Test
    public void getExistingMatches() {
        try (Session session = sessionFactory.openSession()) {
            SelectionQuery<Match> query =
                session.createSelectionQuery("from Match", Match.class);
            List<Match> matches = query.getResultList();
            assertEquals(matches.size(), TestingData.MATCHES_COUNT);
        }
    }

    /** */
    @Test
    public void addNewMatch() {
        long id1 = TestingData.PLAYERS.get(0).id();
        long id2 = TestingData.PLAYERS.get(1).id();

        Match match = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                Player playerZero = session.find(Player.class, id1);
                assertNotNull(playerZero);
                Player playerOne = session.find(Player.class, id2);
                assertNotNull(playerOne);

                match = new Match();
                match.setPlayerOne(playerZero);
                match.setPlayerTwo(playerOne);

                session.persist(match);

                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
        }

        assertNotNull(match.getId());
        assertNotNull(match.getUuid());

        try (Session session = sessionFactory.openSession()) {
            Match newMatch = session
                .createSelectionQuery("from Match where id = ?1", Match.class)
                .setParameter(1, 1)
                .getSingleResultOrNull();

            assertNotNull(newMatch.getPlayerOne());
            assertNotNull(newMatch.getPlayerTwo());
            assertNotNull(newMatch.getPlaysets());

            assertTrue(match.equals(newMatch));
            assertEquals(match.hashCode(), newMatch.hashCode());
        }
    }

    /** */
    @Test
    public void deleteExistingMatch() {
        var sampleMatch = TestingData.MATCHES.get(2);
        sessionFactory.inTransaction(session -> {
            Match match = session.find(Match.class, sampleMatch.id());
            assertNotNull(match);

            session.remove(match);
        });
        sessionFactory.inSession(session -> {
            long matchesCount = session
                .createSelectionQuery("from Match", Match.class)
                .getResultCount();
            assertEquals(matchesCount, TestingData.MATCHES.size() - 1);

            long playsetsCount = session
                .createSelectionQuery("from Playset", Playset.class)
                .getResultCount();
            assertEquals(
                TestingData.PLAYSETS
                    .stream()
                    .filter(p -> !p.matchId().equals(sampleMatch.id()))
                    .count(),
                    playsetsCount
            );
        });
    }

    /** */
    @Test
    public void updateExistingMatch() {
        var sampleMatch = TestingData.MATCHES.get(1);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            UUID newUuid = UUID.randomUUID();
            try {
                tx.begin();

                Match match = session.find(Match.class, sampleMatch.id());
                assertNotNull(match);

                match.setUuid(newUuid);
                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
            Match updatedMatch = session.find(Match.class, sampleMatch.id());

            assertNotNull(updatedMatch);
            assertEquals(updatedMatch.getUuid(), newUuid);
        }
    }

    /** */
    @Test
    public void getExistingPlaysets() {
        sessionFactory.inSession(session -> {
            long size = session
                .createSelectionQuery("from Playset", Playset.class)
                .getResultCount();

            assertEquals(size, TestingData.PLAYSETS.size());
        });
    }

    /** */
    @Test
    public void insertNewPlayset() {
        Playset playset = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                Match match = session
                    .find(Match.class, TestingData.MATCHES.get(2).id());

                assertNotNull(match);

                playset = new Playset();
                playset.setMatch(match);
                playset.setPlayerOneSetScore(0);
                playset.setPlayerTwoSetScore(0);

                session.persist(playset);

                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }

            assertNotNull(playset.getId());
            assertNotNull(playset.getUuid());

            Playset newPlayset = session
                .createSelectionQuery(
                    "from Playset where id = ?1",
                    Playset.class)
                .setParameter(1, playset.getId())
                .getSingleResultOrNull();

            assertNotNull(newPlayset);

            assertTrue(playset.equals(newPlayset));
            assertEquals(playset.hashCode(), newPlayset.hashCode());
        }
    }

    /** */
    @Test
    public void deleteExistingPlayset() {
        var samplePlayset = TestingData.PLAYSETS.get(2);

        sessionFactory.inTransaction(session -> {
            int res = session
                .createMutationQuery("delete Playset where id = ?1")
                .setParameter(1, samplePlayset.id())
                .executeUpdate();
            assertEquals(res, 1);
        });

        sessionFactory.inSession(session -> {
            long count = session
                .createSelectionQuery("from Playset", Playset.class)
                .getResultCount();
            assertEquals(count, TestingData.PLAYSETS.size() - 1);

            long countGames = session
                .createSelectionQuery("from Game", Game.class)
                .getResultCount();
            assertEquals(
                TestingData.GAMES
                    .stream()
                    .filter(g -> !g.playsetId().equals(samplePlayset.id()))
                    .count(),
                countGames
            );

            Match match = session.find(Match.class, samplePlayset.matchId());
            assertEquals(
                TestingData.PLAYSETS
                .stream()
                .filter(p -> p.matchId() == match.getId().longValue())
                .count() - 1, match.getPlaysets().size()
            );
        });
    }

    /** */
    @Test
    public void updateExistingPlayset() {
        var samplePlayset = TestingData.PLAYSETS.getLast();
        UUID newUuid = UUID.randomUUID();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                Playset playset = session
                    .find(Playset.class, samplePlayset.id());
                playset.setUuid(newUuid);
                playset.setPlayerOneSetScore(
                playset.getPlayerOneSetScore() + 1);

                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                    throw ex;
                }
            }

            Playset playset = session
                .createSelectionQuery(
                    "from Playset where id = ?1", Playset.class)
                .setParameter(1, samplePlayset.id())
                .getSingleResultOrNull();
            assertNotNull(playset);
            assertEquals(playset.getUuid(), newUuid);
            assertEquals(
                samplePlayset.playerOneSetScore() + 1,
                playset.getPlayerOneSetScore());
        }
    }

    /** */
    @Test
    public void getExistingGames() {
        sessionFactory.inSession(session -> {
            long size = session
                .createSelectionQuery("from Game", Game.class)
                .getResultCount();

            assertEquals(size, TestingData.GAMES.size());
        });
    }

    /** */
    @Test
    public void insertNewGame() {
        Game game = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                Playset playset = session
                    .find(
                        Playset.class,
                        TestingData.PLAYSETS
                            .get(TestingData.PLAYSETS_COUNT - 1).id());

                assertNotNull(playset);

                game = new Game();
                game.setPlayset(playset);
                game.setPlayerOneGameScore(0);
                game.setPlayerTwoGameScore(0);

                session.persist(game);

                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }

            assertNotNull(game.getId());
            assertNotNull(game.getUuid());

            Game newGame = session
                .createSelectionQuery(
                    "from Game where id = ?1",
                    Game.class)
                .setParameter(1, game.getId())
                .getSingleResultOrNull();

            assertNotNull(newGame);

            assertTrue(game.equals(newGame));
            assertEquals(game.hashCode(), newGame.hashCode());
        }
    }

    /** */
    @Test
    public void deleteExistingGame() {
        var sampleGame = TestingData.GAMES.getLast();
        sessionFactory.inTransaction(session -> {
            Game game = session.find(Game.class, sampleGame.id());
            session.remove(game);
        });
        sessionFactory.inSession(session -> {
            long count = session
                .createSelectionQuery("from Game", Game.class)
                .getResultCount();
            assertEquals(TestingData.GAMES.size() - 1, count);

            Playset playset = session
                .find(Playset.class, sampleGame.playsetId());
            assertEquals(
                TestingData.GAMES
                    .stream()
                    .filter(g -> g.playsetId() == playset.getId().longValue())
                    .count() - 1,
                    playset.getGames().size()
            );
        });
    }

    /** */
    @Test
    public void updateExistingGame() {
        var sampleGame = TestingData.GAMES.getLast();
        UUID newUuid = UUID.randomUUID();
        final int newPlayerOneGameScore = 4;
        final int newPlayerTwoGameScore = 2;
        sessionFactory.inTransaction(session -> {
            Game game = session.find(Game.class, sampleGame.id());
            game.setUuid(newUuid);
            game.setPlayerOneGameScore(newPlayerOneGameScore);
            game.setPlayerTwoGameScore(newPlayerTwoGameScore);
        });
        sessionFactory.inSession(session -> {
            Game game = session.find(Game.class, sampleGame.id());
            assertEquals(newPlayerOneGameScore, game.getPlayerOneGameScore());
            assertEquals(newPlayerTwoGameScore, game.getPlayerTwoGameScore());
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
