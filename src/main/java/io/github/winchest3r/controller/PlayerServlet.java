package io.github.winchest3r.controller;

import java.util.*;
import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

import io.github.winchest3r.model.Player;
import io.github.winchest3r.util.SimpleServletContextLogger;

/**
 * Servlet to show all players.
 * @deprecated Will be removed after testing.
 */
@WebServlet(name = "PlayersServlet", urlPatterns = "/players")
public class PlayerServlet extends HttpServlet {
    /** List of players. */
    private List<Player> players = new ArrayList<>();

    /** SessionFactory. */
    private static SessionFactory sessionFactory;

    /** Logger. */
    private static SimpleServletContextLogger logger;

    /** Json mapper. */
    private ObjectMapper objectMapper = new ObjectMapper();

    /** Init. */
    @Override
    public void init(final ServletConfig config) {
        logger = new SimpleServletContextLogger(
            config.getServletContext(), getClass()
        );

        sessionFactory =
        new Configuration()
            .buildSessionFactory();
        if (sessionFactory.isOpen()) {
            logger.log("Session is established");
        }
    }

    /**
     * GET to /players.
     */
    @Override
    public void doGet(final HttpServletRequest request,
                      final HttpServletResponse response) {
        response.setContentType("application/json");
        try (Writer out = response.getWriter()) {
            String json = objectMapper.writeValueAsString(players);
            out.write(json);
        } catch (Exception ex) {
            logger.log("An exception occured during HTTP GET", ex);
        }
    }

    /**
     * POST a player.
     */
    @Override
    public void doPost(final HttpServletRequest request,
                       final HttpServletResponse response) {
        Map<String, String[]> params = request.getParameterMap();

        if (params.containsKey("name")) {
            getServletContext().log("playerName=" + params.get("name")[0]);
        }
    }

    /** Close session. */
    @Override
    public void destroy() {
        sessionFactory.close();
    }
}
