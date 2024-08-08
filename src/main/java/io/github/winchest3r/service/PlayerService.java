package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.Set;
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
}
