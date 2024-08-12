package io.github.winchest3r.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

import io.github.winchest3r.service.ScoreboardService;
import io.github.winchest3r.model.Match;


@Named(value = "matches")
@ApplicationScoped
public class Matches implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Scoreboard service. */
    private static ScoreboardService scoreboardService =
        new ScoreboardService();

    /**
     * Get matches in database.
     * @return List of matches.
     */
    public List<Match> getMatches() {
        return scoreboardService.match().getMatches();
    }
}
