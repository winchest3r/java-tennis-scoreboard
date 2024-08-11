package io.github.winchest3r.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

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
                .setProperty(
                    AvailableSettings.JAKARTA_JDBC_DRIVER,
                    "org.h2.Driver")
                .setProperty(
                    AvailableSettings.JAKARTA_JDBC_URL,
                    "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false")
                // Credentials
                .setProperty(AvailableSettings.JAKARTA_JDBC_USER, "sa")
                .setProperty(
                    AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                    Action.SPEC_ACTION_DROP_AND_CREATE)
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
