package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

import io.github.winchest3r.util.HibernateUtil;
import io.github.winchest3r.model.Game;

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
}
