package io.github.winchest3r.service;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

import io.github.winchest3r.util.HibernateUtil;
import io.github.winchest3r.model.Playset;

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
}
