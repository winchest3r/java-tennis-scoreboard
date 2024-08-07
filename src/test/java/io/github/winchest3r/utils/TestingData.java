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

    /**
     * Simple Match class.
     * @param id number id
     * @param playerOneId id
     * @param playerTwoId id
     * @param winnerId id
     */
    public static record Match(
        Integer id,
        Integer playerOneId,
        Integer playerTwoId,
        Integer winnerId
    ) { }

    /**
     * Simple Playset class.
     * @param id
     * @param matchId
     * @param playerOneSetScore
     * @param playerTwoSetScore
     */
    public static record Playset(
        Integer id,
        Integer matchId,
        Integer playerOneSetScore,
        Integer playerTwoSetScore
    ) { }

    /**
     * Simple Game class.
     * @param id
     * @param playsetId
     * @param playerOneGameScore
     * @param playerTwoGameScore
     */
    public static record Game(
        Integer id,
        Integer playsetId,
        Integer playerOneGameScore,
        Integer playerTwoGameScore
    ) { }

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

    /** Matches in test database. */
    public static final List<Match> MATCHES = List.of(
        new Match(9000, 10000, 10001, 10000),
        new Match(9001, 10002, 10003, 10002),
        new Match(9002, 10001, 10003, null)
    );

    /** Playsets in test database. */
    public static final List<Playset> PLAYSETS = List.of(
        new Playset(8000, 9000, 6, 2),
        new Playset(8001, 9000, 6, 3),
        new Playset(8002, 9001, 6, 2),
        new Playset(8003, 9001, 4, 6),
        new Playset(8004, 9001, 7, 6),
        new Playset(8005, 9002, 5, 7),
        new Playset(8006, 9002, 0, 1)
    );

    /** Games in test database. */
    public static final List<Game> GAMES = List.of(
        new Game(7000, 8000, 4, 2),
        new Game(7001, 8000, 5, 3),
        new Game(7002, 8000, 4, 0),
        new Game(7003, 8000, 2, 4),
        new Game(7004, 8000, 4, 6),
        new Game(7005, 8000, 4, 0),
        new Game(7006, 8000, 5, 3),
        new Game(7007, 8000, 4, 0),
        new Game(7008, 8001, 4, 2),
        new Game(7009, 8001, 6, 4),
        new Game(7010, 8001, 3, 5),
        new Game(7011, 8001, 4, 0),
        new Game(7012, 8001, 1, 4),
        new Game(7013, 8001, 2, 4),
        new Game(7014, 8001, 4, 1),
        new Game(7015, 8001, 7, 5),
        new Game(7016, 8001, 6, 4),
        new Game(7017, 8002, 3, 5),
        new Game(7018, 8002, 2, 4),
        new Game(7019, 8002, 6, 4),
        new Game(7020, 8002, 5, 3),
        new Game(7021, 8002, 4, 2),
        new Game(7022, 8002, 4, 1),
        new Game(7023, 8002, 4, 2),
        new Game(7024, 8002, 5, 3),
        new Game(7025, 8003, 5, 3),
        new Game(7026, 8003, 4, 2),
        new Game(7027, 8003, 3, 5),
        new Game(7028, 8003, 3, 5),
        new Game(7029, 8003, 6, 4),
        new Game(7030, 8003, 4, 2),
        new Game(7031, 8003, 3, 5),
        new Game(7032, 8003, 1, 4),
        new Game(7033, 8003, 0, 4),
        new Game(7034, 8003, 3, 5),
        new Game(7035, 8004, 6, 4),
        new Game(7036, 8004, 6, 4),
        new Game(7037, 8004, 5, 3),
        new Game(7038, 8004, 4, 0),
        new Game(7039, 8004, 2, 4),
        new Game(7040, 8004, 6, 8),
        new Game(7041, 8004, 4, 2),
        new Game(7042, 8004, 9, 7),
        new Game(7043, 8004, 4, 6),
        new Game(7044, 8004, 3, 5),
        new Game(7045, 8004, 2, 4),
        new Game(7046, 8004, 5, 7),
        new Game(7047, 8004, 9, 7),
        new Game(7048, 8005, 5, 3),
        new Game(7049, 8005, 6, 4),
        new Game(7050, 8005, 3, 5),
        new Game(7051, 8005, 2, 4),
        new Game(7052, 8005, 1, 4),
        new Game(7053, 8005, 2, 4),
        new Game(7054, 8005, 5, 3),
        new Game(7055, 8005, 5, 3),
        new Game(7056, 8005, 6, 4),
        new Game(7057, 8005, 3, 5),
        new Game(7058, 8005, 2, 4),
        new Game(7059, 8005, 0, 4),
        new Game(7060, 8006, 0, 4)
    );
}
