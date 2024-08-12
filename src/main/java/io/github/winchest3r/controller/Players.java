package io.github.winchest3r.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

import io.github.winchest3r.service.PlayerService;
import io.github.winchest3r.model.Player;

@Named(value = "players")
@ApplicationScoped
public class Players implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Player Service. */
    private static PlayerService playerService = new PlayerService();

    /** New player name field. */
    private String newPlayerName;

    /** Init constructor. */
    public Players() {
        playerService.addNewPlayer("John Smith");
        playerService.addNewPlayer("Mike Vazovsky");
        playerService.addNewPlayer("Lara Parker");
        newPlayerName = "";
    }

    /**
     * Get players in database.
     * @return List of players.
     */
    public List<Player> getPlayers() {
        return playerService.getPlayers();
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
        if (!newPlayerName.isEmpty()) {
            playerService.addNewPlayer(newPlayerName);
            setNewPlayerName("");
        } else {
            FacesContext.getCurrentInstance().addMessage(
                "newPlayerForm:newPlayerName",
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Name can't be empty", null
                )
            );
        }
    }
}
