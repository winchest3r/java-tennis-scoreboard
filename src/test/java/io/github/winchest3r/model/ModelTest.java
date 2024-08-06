package io.github.winchest3r.model;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.MutationQuery;
import org.hibernate.tool.schema.Action;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.random.RandomGenerator;
import java.util.List;

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
    public void canFindPlayer() {
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
    public void canGetMatches() {
        try (Session session = sessionFactory.openSession()) {
            SelectionQuery<Match> query =
                session.createSelectionQuery("from Match", Match.class);
            List<Match> matches = query.getResultList();
            assertEquals(matches.size(), TestingData.MATCHES_COUNT);
        }
    }

    /** */
    @Test
    public void canAddNewMatch() {
        long id1 = TestingData.PLAYERS.get(0).id();
        long id2 = TestingData.PLAYERS.get(1).id();

        sessionFactory.inTransaction(session -> {
            Player playerZero = session.find(Player.class, id1);
            assertNotNull(playerZero);
            Player playerOne = session.find(Player.class, id2);
            assertNotNull(playerOne);

            Match match = new Match();
            match.setPlayerOne(playerZero);
            match.setPlayerTwo(playerOne);

            session.persist(match);
        });
        sessionFactory.inSession(session -> {
            Match match = session
                .createSelectionQuery("from Match where id = ?1", Match.class)
                .setParameter(1, 1)
                .getSingleResultOrNull();
            assertNotNull(match);
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
