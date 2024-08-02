package io.github.winchest3r.controllers;

import java.util.*;
import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.*;

import io.github.winchest3r.models.Player;

@WebServlet(name="PlayersServlet", urlPatterns="/players")
public class PlayersServlet extends HttpServlet {
    private List<Player> players = new ArrayList<>();
    private Connection conn;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) {
        try {
            org.h2.Driver.load();
            conn = DriverManager.getConnection("jdbc:h2:./db", "sa", "");
            config.getServletContext().log(
                getClass().getName() + ": Database initialized"
            );
        } catch (Exception ex) {
            config.getServletContext().log(
                getClass().getName() + ": Can't establish a connection", ex
            );
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        try (Writer out = response.getWriter()) {
            String json = objectMapper.writeValueAsString(players);
            out.write(json);
        } catch (Exception ex) {
            getServletContext().log(getClass().getName() + " An exception occured during HTTP GET", ex);
        }
    }
}
