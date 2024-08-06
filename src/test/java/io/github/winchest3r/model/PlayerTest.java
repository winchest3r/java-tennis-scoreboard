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
            SelectionQuery<Player> query =
                session.createSelectionQuery("from Player", Player.class);
            assertEquals(TestingData.PLAYERS.size(), query.getResultCount());
            for (Player p : query.getResultList()) {
                assertTrue(TestingData.PLAYERS.contains(p.getName()));
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
            String samplePlayerName = TestingData.PLAYERS.get(randIdx);

            Player player = session
                .createSelectionQuery(
                    "from Player where name = ?1",
                    Player.class)
                .setParameter(1, samplePlayerName)
                .getSingleResultOrNull();

            assertNotNull(player);
            assertEquals(player.getName(), samplePlayerName);
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
        String playerName = TestingData.PLAYERS.get(1);
        try (Session session = sessionFactory.openSession()) {
            var tx = session.getTransaction();
            try {
                tx.begin();
                MutationQuery query = session.createMutationQuery(
                    "delete Player where name = ?1"
                );
                query.setParameter(1, playerName);
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

            assertNull(newPlayer);
        }
    }

    /** */
    @Test
    public void updateExistingPlayer() {
        String playerName = TestingData.PLAYERS.get(2);
        String newName = "Super Player";
        try (Session session = sessionFactory.openSession()) {
            var tx = session.getTransaction();
            try {
                tx.begin();
                Player player = session
                    .createSelectionQuery(
                        "from Player where name = ?1",
                        Player.class)
                    .setParameter(1, playerName)
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
    @AfterEach
    public void closeSession() {
        if (sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
