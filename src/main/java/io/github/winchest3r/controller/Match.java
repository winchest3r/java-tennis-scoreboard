package io.github.winchest3r.controller;

import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;

import java.util.UUID;
import java.util.Map;

import io.github.winchest3r.service.ScoreboardService;

@Named(value = "match")
@RequestScoped
public class Match {
    /** Scoreboard service. */
    private static ScoreboardService scoreboardService =
        new ScoreboardService();

    /** UUID from parameter. */
    private UUID uuid;

    /** Match with current uuid. */
    private io.github.winchest3r.model.Match match;

    /** Constructor. */
    public Match() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context
            .getExternalContext()
            .getRequestParameterMap();
        uuid = UUID.fromString(params.get("uuid"));
        match = scoreboardService.match().getMatchByUuid(uuid);
    }

    /**
     * Get match info.
     * @return Match model.
     */
    public io.github.winchest3r.model.Match getMatch() {
        return match;
    }
}
