package io.github.winchest3r.utils;

import java.util.List;

/**
 * Contains information from sql\tennis-test-dataset.sql.
 */
public final class TestingData {
    /**
     * Simple Player class.
     * @param id number
     * @param name string
     */
    public static record Player(long id, String name) { }

    private TestingData() { }

    /** */
    public static final int PLAYERS_COUNT = 4;

    /** */
    public static final int MATCHES_COUNT = 3;

    /** */
    public static final int PLAYSETS_COUNT = 7;

    /** */
    public static final int GAMES_COUNT = 61;

    /** Player's list with ids related to playerId from dataset. */
    public static final List<Player> PLAYERS = List.of(
        new Player(10000, "Player Zero"),
        new Player(10000, "Player One"),
        new Player(10000, "Player Two"),
        new Player(10000, "Player Three")
    );
}
