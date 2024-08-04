package io.github.winchest3r.utils;

import jakarta.servlet.ServletContext;

public class SimpleServletContextLogger {
    /** Class to message for. */
    private Class<?> cl;

    /** Servlet context to get context logger. */
    private ServletContext context;

    /**
     * Constructor with class and context.
     * @param servletContext context
     * @param objClass class
     */
    public SimpleServletContextLogger(
            final ServletContext servletContext,
            final Class<?> objClass) {
        this.cl = objClass;
        this.context = servletContext;
    }

    /**
     * Logs message using servlet context.
     * @param message String with message.
     */
    public void log(final String message) {
        this.context.log(cl.getName() + ": " + message);
    }

    /**
     * Logs message using servlet context and throwable object.
     * @param message String with message.
     * @param t Exception.
     */
    public void log(final String message, final Throwable t) {
        this.context.log(cl.getName() + ": " + message, t);
    }
}
