package io.github.winchest3r.controller;

import java.util.*;
import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.winchest3r.model.Player;

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
    private static Logger logger = LoggerFactory.getLogger(PlayerServlet.class);

    /** Json mapper. */
    private ObjectMapper objectMapper = new ObjectMapper();

    /** Init. */
    @Override
    public void init(final ServletConfig config) {
        sessionFactory =
        new Configuration()
            .buildSessionFactory();
        if (sessionFactory.isOpen()) {
            logger.info("Session is established");
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
            getServletContext().log(
                getClass().getName()
                    + " An exception occured during HTTP GET", ex);
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
}
