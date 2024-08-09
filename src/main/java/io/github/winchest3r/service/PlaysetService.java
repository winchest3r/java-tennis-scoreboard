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
import io.github.winchest3r.model.Player;

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
        // TODO
    }

    /**
     * Get playset using it's UUID.
     * @param uuid Playset's UUID.
     * @return Playset.
     */
    public Playset getPlaysetByUuid(final UUID uuid) {
        // TODO
    }

    /**
     * Add new playset for selected match.
     * @param match Selected match.
     * @return Initialized new playset.
     */
    public Playset addNewPlayset(final Match match) {
        // TODO
    }

    /**
     * Change set score of selected player.
     * @param match Selected match.
     * @param player Selected player.
     * @param score New score of selected player.
     */
    public void setNewSetScore(
            final Match match,
            final Player player,
            final Integer score) {
        // TODO
    }
}
