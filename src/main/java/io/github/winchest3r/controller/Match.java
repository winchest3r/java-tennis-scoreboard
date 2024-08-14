package io.github.winchest3r.controller;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;

import io.github.winchest3r.service.ScoreboardService;
import io.github.winchest3r.model.Playset;
import io.github.winchest3r.model.Game;
import io.github.winchest3r.util.ScoreboardUtil;

@Named(value = "match")
@ViewScoped
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Scoreboard service. */
    private static ScoreboardService scoreboardService =
        new ScoreboardService();

    /** UUID from parameter. */
    private UUID uuid;

    /** Match with current uuid. */
    private io.github.winchest3r.model.Match match;

    /** Playsets related to match. */
    private List<Playset> sets;

    /** List of games that related to currentSet. */
    private List<Game> games;

    /** Current set that is ongoing. */
    private Playset currentSet;

    /** Current game that is ongoing. */
    private Game currentGame;

    /** Constructor. */
    public Match() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context
            .getExternalContext()
            .getRequestParameterMap();
        if (params.containsKey("uuid")) {
            uuid = UUID.fromString(params.get("uuid"));
            match = scoreboardService.match().getMatchByUuid(uuid);
            if (match != null) {
                initPlaysets();
            }
        }
    }

    private void initPlaysets() {
        var matchPlaysets = scoreboardService
            .playset()
            .getPlaysetsByMatch(match);
        sets = new ArrayList<>(matchPlaysets
            .stream()
            .sorted((a, b) ->
                a.getStartTime().compareTo(b.getStartTime()))
            .toList()
        );
    }

    /**
     * Get match info.
     * @return Match model.
     */
    public io.github.winchest3r.model.Match getMatch() {
        return match;
    }

    /**
     * Get playsets related to match.
     * @return List of playsets ordered by playset start time.
     */
    public List<Playset> getSets() {
        return sets;
    }

    /**
     * Get UUID.
     * @return UUID.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get ongoing set.
     * @return Playset.
     */
    public Playset getCurrentSet() {
        if (currentSet != null) {
            return currentSet;
        }
        if (!sets.isEmpty()) {
            int firstScore = sets.getLast().getPlayerOneSetScore();
            int secondScore = sets.getLast().getPlayerTwoSetScore();
            if (ScoreboardUtil.setScoreResult(firstScore, secondScore) == 0) {
                currentSet = sets.getLast();
                return currentSet;
            }
        }
        return null;
    }

    /**
     * Get games related to current set.
     * @return List of games or null.
     */
    public List<Game> getGames() {
        if (games != null) {
            return games;
        }
        if (currentSet != null) {
            games = scoreboardService
                .game()
                .getGamesByPlayset(currentSet);
        }
        return null;
    }

    /**
     * Get current ongoing game.
     * @return Game or null.
     */
    public Game getCurrentGame() {
        if (currentGame != null) {
            return currentGame;
        }
        if (currentSet != null) {
            List<Game> localGames = games != null ? games
                : scoreboardService
                    .game()
                    .getGamesByPlayset(currentSet);
            // Get closest game by time.
            currentGame = localGames
                .stream()
                .max((a, b) -> a
                    .getStartTime()
                    .compareTo(b.getStartTime()))
                .orElse(null);
            // If we didn't find any game - need to create new one.
            if (currentGame == null) {
                currentGame = scoreboardService.game().addNewGame(currentSet);
            }
            return currentGame;
        }
        return null;
    }

    /** Action to start new set. */
    public void startNewSet() throws IOException {
        currentSet = scoreboardService.playset().addNewPlayset(match);
        System.out.println(currentSet + " is added. Start redirection.");
        FacesContext context = FacesContext.getCurrentInstance();
                context
                    .getExternalContext()
                    .redirect("match.xhtml?uuid=" + match.getUuid());
    }

    /**
     * Calculate result of score and update it for match/set/game.
     * @param player -1 if player one and 1 if player two.
     */
    public void playerGameScore(final Integer player) throws IOException {
        // Add point to players
        if (player == -1) {
            scoreboardService
                .game()
                .setPlayerOneGameScore(
                    currentGame,
                    currentGame.getPlayerOneGameScore() + 1);
        } else if (player == 1) {
            scoreboardService
                .game()
                .setPlayerTwoGameScore(
                    currentGame,
                    currentGame.getPlayerTwoGameScore() + 1);
        }
        Integer playerOneNewGameScore =
            currentGame.getPlayerOneGameScore()
                + (player == -1 ? 1 : 0);
        Integer playerTwoNewGameScore =
            currentGame.getPlayerTwoGameScore()
                + (player == 1 ? 1 : 0);
        // Check winner state.
        boolean isTiebreak =
            currentSet.getPlayerOneSetScore() == 6
            && currentSet.getPlayerTwoSetScore() == 6;
        int gameScoreResult = ScoreboardUtil
            .gameScoreResult(
                playerOneNewGameScore,
                playerTwoNewGameScore);
        int tiebreakResult = ScoreboardUtil
            .tiebreakScoreResult(
                playerOneNewGameScore,
                playerTwoNewGameScore);
        // If someone won - update set score.
        if ((!isTiebreak && gameScoreResult != 0)
                || (isTiebreak && tiebreakResult != 0)) {
            int winner = isTiebreak ? tiebreakResult : gameScoreResult;
            if (winner == -1) {
                scoreboardService
                    .playset()
                    .setPlayerOneSetScore(
                        currentSet,
                        currentSet.getPlayerOneSetScore() + 1);
            } else if (winner == 1) {
                scoreboardService
                    .playset()
                    .setPlayerTwoSetScore(
                        currentSet,
                        currentSet.getPlayerTwoSetScore() + 1);
            }
            // Game is over.
            currentGame = null;
            // Check if set is over.
            int playerOneNewSetScore =
                currentSet.getPlayerOneSetScore()
                    + (winner == -1 ? 1 : 0);
            int playerTwoNewSetScore =
                currentSet.getPlayerTwoSetScore()
                    + (winner == 1 ? 1 : 0);
            int setScoreResult = ScoreboardUtil.setScoreResult(
                playerOneNewSetScore,
                playerTwoNewSetScore);
            if (setScoreResult != 0) {
                currentSet = null;
                // Check if match is over and set winner.
                int playerOneMatchScore = (int) sets
                    .stream()
                    .filter(s -> ScoreboardUtil
                        .setScoreResult(
                            s.getPlayerOneSetScore(),
                            s.getPlayerTwoSetScore()) < 0)
                    .count() + (setScoreResult == -1 ? 1 : 0);
                int playerTwoMatchScore = (int) sets
                    .stream()
                    .filter(s -> ScoreboardUtil
                        .setScoreResult(
                            s.getPlayerOneSetScore(),
                            s.getPlayerTwoSetScore()) > 0)
                    .count() + (setScoreResult == 1 ? 1 : 0);
                int matchScoreResult = ScoreboardUtil
                    .matchScoreResult(playerOneMatchScore, playerTwoMatchScore);
                // If someone won - set him in match winner.
                if (matchScoreResult != 0) {
                    scoreboardService
                        .match()
                        .setMatchWinner(
                            match,
                            matchScoreResult == -1
                                ? match.getPlayerOne()
                                : match.getPlayerTwo());
                }
            // if set is not over - create new game.
            } else {
                currentGame = scoreboardService
                    .game()
                    .addNewGame(currentSet);
            }
        }

        FacesContext context = FacesContext.getCurrentInstance();
                context
                    .getExternalContext()
                    .redirect("match.xhtml?uuid=" + match.getUuid());
    }
}
