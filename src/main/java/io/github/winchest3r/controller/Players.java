package io.github.winchest3r.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

import io.github.winchest3r.service.PlayerService;
import io.github.winchest3r.model.Player;

@Named
@RequestScoped
public class Players implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Player Service. */
    private static PlayerService playerService = new PlayerService();

    /** List of existing players. */
    private List<Player> players;

    /** New player name field. */
    private String newPlayerName;

    /** Init constructor. */
    public Players() {
        playerService.addNewPlayer("John Smith");
        playerService.addNewPlayer("Mike Vazovsky");
        playerService.addNewPlayer("Lara Parker");
        players = playerService.getPlayers();
        newPlayerName = "";
    }

    /**
     * Get players in database.
     * @return List of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Set new list of players.
     * @param newPlayers New list of players.
     */
    public void setPlayers(final List<Player> newPlayers) {
        players = newPlayers;
    }

    /**
     * Get new player name field value.
     * @return New player name field value.
     */
    public String getNewPlayerName() {
        return newPlayerName;
    }

    /**
     * Set new player name field value.
     * @param newNewPlayerName New player name field value.
     */
    public void setNewPlayerName(final String newNewPlayerName) {
        newPlayerName = newNewPlayerName;
    }

    /** Method to add new player in database. */
    public void addNewPlayer() {
        playerService.addNewPlayer(newPlayerName);
    }
}
