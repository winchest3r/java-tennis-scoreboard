package io.github.winchest3r.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.winchest3r.service.ScoreboardService;
import io.github.winchest3r.model.Player;

@Named(value = "newMatch")
@ApplicationScoped
public class NewMatch {
    /** All services. */
    private static ScoreboardService scoreboardService =
        new ScoreboardService();

    /** Pattern to parse player's name. */
    private static Pattern pattern =
        Pattern.compile("^\\[(\\w+\\s+){1,}:\\s+([0-9a-zA-Z-]+)\\]$");

    /** Field of player one. */
    private String playerOne;
    /** Field of player two. */
    private String playerTwo;

    /** New match controller constructor. */
    public NewMatch() { }

    /**
     * Get players for selection.
     * @return List of selection items.
     */
    public List<Player> getPlayers() {
        List<SelectItem> fetchPlayers = new ArrayList<>();
        for (Player p : scoreboardService.player().getPlayers()) {
            fetchPlayers.add(new SelectItem(p));
        }
        return scoreboardService.player().getPlayers();
    }

    /**
     * Get player one.
     * @return Player.
     */
    public String getPlayerOne() {
        return playerOne;
    }

    /**
     * Set player one.
     * @param newPlayerOne New player one.
     */
    public void setPlayerOne(final String newPlayerOne) {
        playerOne = newPlayerOne;
    }

    /**
     * Get player two.
     * @return Player two.
     */
    public String getPlayerTwo() {
        return playerTwo;
    }

    /**
     * Set player two.
     * @param newPlayerTwo New player two.
     */
    public void setPlayerTwo(final String newPlayerTwo) {
        playerTwo = newPlayerTwo;
    }

    /** Add new match. */
    public void addNewMatch() {
        if (playerOne == null || playerTwo == null) {
            FacesContext.getCurrentInstance().addMessage(
                "newMatchForm",
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Select both players", null
                )
            );
        } else if (playerOne.equals(playerTwo)) {
            FacesContext.getCurrentInstance().addMessage(
                "newMatchForm",
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Players can't be equal", null
                )
            );
        } else {
            System.out.println(playerOne);
            System.out.println(playerTwo);
            Matcher p1m = pattern.matcher(playerOne);
            Matcher p2m = pattern.matcher(playerTwo);
            if (p1m.matches() && p2m.matches()) {
                Player playerOneModel =
                    scoreboardService
                        .player()
                        .getPlayerByUuid(
                            UUID.fromString(p1m.group(2)
                    ));
                Player playerTwoModel =
                    scoreboardService
                        .player()
                        .getPlayerByUuid(
                            UUID.fromString(p2m.group(2)
                    ));
                scoreboardService
                    .match()
                    .addNewMatch(playerOneModel, playerTwoModel);
            }
        }
    }
}
