package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

import io.github.winchest3r.util.HibernateUtil;
import io.github.winchest3r.model.Player;

public final class PlayerService {
    /** Session factory. */
    private SessionFactory sessionFactory;

    /**
     *  Default service constructor
     *  with initialized session factory.
     */
    public PlayerService() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Constructor with custom session factory (for tests).
     * @param newSessionFactory testing session factory.
     */
    public PlayerService(final SessionFactory newSessionFactory) {
        this.sessionFactory = Objects.requireNonNull(newSessionFactory);
    }

    /**
     * Get all players.
     * @return Set of players.
     */
    public List<Player> getPlayers() {
        List<Player> result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery("from Player", Player.class)
                .getResultList();
        }
        return result;
    }

    /**
     * Find player by UUID or null.
     * @param uuid {@link UUID}
     * @return Player or null if there is no one.
     */
    public Player getPlayerByUuid(final UUID uuid) {
        Player result = null;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery(
                    "from Player where uuid = ?1",
                    Player.class)
                .setParameter(1, uuid)
                .getSingleResultOrNull();
        }
        return result;
    }

    /**
     * Add new player. Names can be similar.
     * @param name Player's name.
     * @return Initialized player with UUID.
     */
    public Player addNewPlayer(final String name) {
        Player result = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                result = new Player();
                result.setName(name);
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
}
