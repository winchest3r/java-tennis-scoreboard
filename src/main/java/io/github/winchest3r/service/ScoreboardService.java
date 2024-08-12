package io.github.winchest3r.service;

public class ScoreboardService {
    /** */
    private GameService gameService;
    /** */
    private MatchService matchService;
    /** */
    private PlayerService playerService;
    /** */
    private PlaysetService playsetService;

    /** Initialize all services. */
    public ScoreboardService() {
        gameService = new GameService();
        matchService = new MatchService();
        playerService = new PlayerService();
        playsetService = new PlaysetService();
    }

    /**
     * Get game service.
     * @return GameService.
     */
    public GameService game() {
        return gameService;
    }

    /**
     * Get match service.
     * @return MatchService.
     */
    public MatchService match() {
        return matchService;
    }

    /**
     * Get player service.
     * @return PlayerService.
     */
    public PlayerService player() {
        return playerService;
    }

    /**
     * Get playset service.
     * @return PlaysetService.
     */
    public PlaysetService playset() {
        return playsetService;
    }
}
