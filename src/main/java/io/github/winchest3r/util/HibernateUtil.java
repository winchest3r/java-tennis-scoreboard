package io.github.winchest3r.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import io.github.winchest3r.model.Player;
import io.github.winchest3r.model.Match;
import io.github.winchest3r.model.Playset;
import io.github.winchest3r.model.Game;

public final class HibernateUtil {
    private HibernateUtil() { }

    /** Session factory object. */
    private static final SessionFactory SESSION_FACTORY;

    // Session factory initialization
    static {
        try {
            SESSION_FACTORY = new Configuration()
                // Add entities to the model
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(Match.class)
                .addAnnotatedClass(Playset.class)
                .addAnnotatedClass(Game.class)
                // All other options in hibernate.properties
                // Build
                .buildSessionFactory();
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Return initialized session factory to further use.
     * @return Initialized {@link SessionFactory}.
     */
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
