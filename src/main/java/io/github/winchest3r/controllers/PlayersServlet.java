package io.github.winchest3r.controllers;

import java.util.*;
import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.*;

import io.github.winchest3r.models.Player;

/**
 * Servlet to show all players.
 * @deprecated Will be removed after testing.
 */
@WebServlet(name = "PlayersServlet", urlPatterns = "/players")
public class PlayersServlet extends HttpServlet {
    /** List of players. */
    private List<Player> players = new ArrayList<>();

    /** Connection. */
    private Connection conn;

    /** Json mapper. */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initialize database.
     */
    @Override
    public void init(final ServletConfig config) {
        try {
            org.h2.Driver.load();
            conn = DriverManager.getConnection("jdbc:h2:./db", "sa", "");
            if (!conn.isClosed()) {
                config.getServletContext().log(
                    getClass().getName() + ": Database initialized"
                );
            }
        } catch (Exception ex) {
            config.getServletContext().log(
                getClass().getName() + ": Can't establish a connection", ex
            );
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
}
