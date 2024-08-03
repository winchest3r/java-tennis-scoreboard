package io.github.winchest3r.controller;

import java.util.*;
import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.*;

import io.github.winchest3r.model.Player;

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
