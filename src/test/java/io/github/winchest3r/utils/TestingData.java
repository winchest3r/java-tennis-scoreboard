package io.github.winchest3r.utils;

import java.util.List;

/**
 * Contains information from sql\tennis-test-dataset.sql.
 */
public final class TestingData {
    private TestingData() { }

    /** */
    public static final int PLAYERS_COUNT = 4;

    /** */
    public static final int MATCHES_COUNT = 3;

    /** */
    public static final int PLAYSETS_COUNT = 7;

    /** */
    public static final int GAMES_COUNT = 61;

    /** Player's list with indexes related to playerId from dataset. */
    public static final List<String> PLAYERS = List.of(
        "Player Zero",
        "Player One",
        "Player Two",
        "Player Three"
    );
}
