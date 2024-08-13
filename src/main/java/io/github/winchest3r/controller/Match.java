package io.github.winchest3r.controller;

import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import io.github.winchest3r.service.ScoreboardService;
import io.github.winchest3r.model.Playset;
import io.github.winchest3r.model.Game;

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

    /** Playsets related to match. */
    private List<Playset> sets;

    /** Set that is ongoing. */
    private Playset currentSet;

    /** Constructor. */
    public Match() { }

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
     * Get ongoing set.
     * @return Playset.
     */
    public Playset getCurrentSet() {
        return currentSet;
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
}
