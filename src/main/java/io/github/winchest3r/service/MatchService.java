package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

import io.github.winchest3r.util.HibernateUtil;
import io.github.winchest3r.model.Match;
import io.github.winchest3r.model.Player;

public class MatchService {
    /** Session factory. */
    private SessionFactory sessionFactory;

    /**
     *  Default service constructor
     *  with initialized session factory.
     */
    public MatchService() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Constructor with custom session factory (for tests).
     * @param newSessionFactory testing session factory.
     */
    public MatchService(final SessionFactory newSessionFactory) {
        this.sessionFactory = Objects.requireNonNull(newSessionFactory);
    }

    /**
     * Get all matches.
     * @return List of matches.
     */
    public List<Match> getMatches() {
        List<Match> result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery("from Match", Match.class)
                .getResultList();
        }
        return result;
    }

    /**
     * Get match related to UUID or null.
     * @param uuid
     * @return Match or null.
     */
    public Match getMatchByUuid(final UUID uuid) {
        Match result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery("from Match where uuid = ?1", Match.class)
                .setParameter(1, uuid)
                .getSingleResultOrNull();
        }
        return result;
    }

    /**
     * Get all matches related to specific user.
     * @param player Player
     * @return List of matches.
     */
    public List<Match> getMatchesByPlayer(final Player player) {
        List<Match> result;
        try (Session session = sessionFactory.openSession()) {
            result = session
                .createSelectionQuery(
                    "from Match where playerOne = ?1 or playerTwo = ?1",
                    Match.class)
                .setParameter(1, Objects.requireNonNull(player))
                .getResultList();
        }
        return result;
    }

    /**
     * Add new match for related players.
     * @param playerOne First player.
     * @param playerTwo Second player.
     * @return Initialized new match.
     */
    public Match addNewMatch(final Player playerOne, final Player playerTwo) {
        Match result = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            try {
                tx.begin();

                result = new Match();
                result.setPlayerOne(playerOne);
                result.setPlayerTwo(playerTwo);
                result.setWinner(null);
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
