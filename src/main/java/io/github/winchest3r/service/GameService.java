package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

import io.github.winchest3r.util.HibernateUtil;
import io.github.winchest3r.model.Game;
import io.github.winchest3r.model.Playset;

public class GameService {
    /** Session factory. */
    private SessionFactory sessionFactory;

    /**
     *  Default service constructor
     *  with initialized session factory.
     */
    public GameService() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Constructor with custom session factory (for tests).
     * @param newSessionFactory testing session factory.
     */
    public GameService(final SessionFactory newSessionFactory) {
        this.sessionFactory = Objects.requireNonNull(newSessionFactory);
    }

    /**
     * Get games related to selected playset.
     * @param playset Selected playset.
     * @return List of games.
     */
    public List<Game> getGamesByPlayset(final Playset playset) {
        List<Game> result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery(
                    "from Game where playset = ?1",
                    Game.class)
                .setParameter(1, playset)
                .getResultList();
        }
        return result;
    }

    /**
     * Get game with selected UUID.
     * @param uuid UUID.
     * @return Selected game or null.
     */
    public Game getGameByUuid(final UUID uuid) {
        Game result = null;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery(
                    "from Game where uuid = ?1",
                    Game.class)
                .setParameter(1, uuid)
                .getSingleResultOrNull();
        }
        return result;
    }

    /**
     * Add new game for selected playset.
     * @param playset Selected playset.
     * @return Initialized new game.
     */
    public Game addNewGame(final Playset playset) {
        Game result;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                result = new Game();
                result.setPlayset(playset);
                result.setPlayerOneGameScore(0);
                result.setPlayerTwoGameScore(0);
                session.persist(result);

                tx.commit();
            } catch (Exception ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
        }
        return result;
    }

    /**
     * Set new score for player one.
     * @param game Selected game.
     * @param score New score for player one.
     */
    public void setPlayerOneGameScore(final Game game, final Integer score) {
        sessionFactory.inTransaction(session -> {
            Game managedGame = session.get(Game.class, game.getId());
            managedGame.setPlayerOneGameScore(score);
        });
    }

    /**
     * Set new score for player two.
     * @param game Selected game.
     * @param score New score for player two.
     */
    public void setPlayerTwoGameScore(final Game game, final Integer score) {
        sessionFactory.inTransaction(session -> {
            Game managedGame = session.get(Game.class, game.getId());
            managedGame.setPlayerTwoGameScore(score);
        });
    }
}
