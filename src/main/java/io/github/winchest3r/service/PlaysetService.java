package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

import io.github.winchest3r.util.HibernateUtil;
import io.github.winchest3r.model.Playset;
import io.github.winchest3r.model.Match;

public class PlaysetService {
    /** Session factory. */
    private SessionFactory sessionFactory;

    /**
     *  Default service constructor
     *  with initialized session factory.
     */
    public PlaysetService() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Constructor with custom session factory (for tests).
     * @param newSessionFactory testing session factory.
     */
    public PlaysetService(final SessionFactory newSessionFactory) {
        this.sessionFactory = Objects.requireNonNull(newSessionFactory);
    }

    /**
     * Get playsets related to selected match.
     * @param match Selected match.
     * @return List of playsets.
     */
    public List<Playset> getPlaysetsByMatch(final Match match) {
        List<Playset> result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery(
                    "from Playset where match = ?1",
                    Playset.class)
                .setParameter(1, match)
                .getResultList();
        }
        return result;
    }

    /**
     * Get playset using it's UUID.
     * @param uuid Playset's UUID.
     * @return Playset or null.
     */
    public Playset getPlaysetByUuid(final UUID uuid) {
        Playset result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery(
                    "from Playset where uuid = ?1",
                    Playset.class)
                .setParameter(1, uuid)
                .getSingleResultOrNull();
        }
        return result;
    }

    /**
     * Add new playset for selected match.
     * @param match Selected match.
     * @return Initialized new playset.
     */
    public Playset addNewPlayset(final Match match) {
        Playset result;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                result = new Playset();
                result.setMatch(match);
                result.setPlayerOneSetScore(0);
                result.setPlayerTwoSetScore(0);
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
     * Change set score of player one.
     * @param playset Selected playset.
     * @param score New score of player one.
     */
    public void setPlayerOneSetScore(
            final Playset playset,
            final Integer score) {
        sessionFactory.inTransaction(session -> {
            playset.setPlayerOneSetScore(score);
        });
    }

    /**
     * Change set score of player two.
     * @param playset Selected playset.
     * @param score New score of player two.
     */
    public void setPlayerTwoSetScore(
            final Playset playset,
            final Integer score) {
        sessionFactory.inTransaction(session -> {
            playset.setPlayerTwoSetScore(score);
        });
    }
}
