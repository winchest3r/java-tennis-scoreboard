package io.github.winchest3r.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardUtilTest {
    private static record TripleData(int one, int two, int result) { }

    /** */
    private static final List<TripleData> MATCH_RESULTS = List.of(
        new TripleData(0, 0, 0),
        new TripleData(1, 0, 0),
        new TripleData(2, 0, -1),
        new TripleData(0, 1, 0),
        new TripleData(0, 2, 1),
        new TripleData(1, 1, 0),
        new TripleData(2, 1, -1),
        new TripleData(1, 2, 1)
    );

    /** */
    private static final List<TripleData> SET_RESULTS = new ArrayList<>();

    /** */
    private static final List<TripleData> GAME_RESULTS = new ArrayList<>();

    /** */
    private static final List<TripleData> TIEBREAK_RESULTS = new ArrayList<>();

    static {
        // Add test results for sets.
        SET_RESULTS.add(new TripleData(0, 0, 0));
        SET_RESULTS.add(new TripleData(1, 0, 0));
        SET_RESULTS.add(new TripleData(2, 0, 0));
        SET_RESULTS.add(new TripleData(3, 0, 0));
        SET_RESULTS.add(new TripleData(4, 0, 0));
        SET_RESULTS.add(new TripleData(5, 0, 0));
        SET_RESULTS.add(new TripleData(6, 0, -1));
        SET_RESULTS.add(new TripleData(0, 1, 0));
        SET_RESULTS.add(new TripleData(0, 2, 0));
        SET_RESULTS.add(new TripleData(0, 3, 0));
        SET_RESULTS.add(new TripleData(0, 4, 0));
        SET_RESULTS.add(new TripleData(0, 5, 0));
        SET_RESULTS.add(new TripleData(0, 6, 1));
        SET_RESULTS.add(new TripleData(1, 1, 0));
        SET_RESULTS.add(new TripleData(2, 2, 0));
        SET_RESULTS.add(new TripleData(3, 3, 0));
        SET_RESULTS.add(new TripleData(4, 4, 0));
        SET_RESULTS.add(new TripleData(5, 5, 0));
        SET_RESULTS.add(new TripleData(6, 6, 0));
        SET_RESULTS.add(new TripleData(1, 2, 0));
        SET_RESULTS.add(new TripleData(2, 1, 0));
        SET_RESULTS.add(new TripleData(3, 1, 0));
        SET_RESULTS.add(new TripleData(1, 3, 0));
        SET_RESULTS.add(new TripleData(3, 2, 0));
        SET_RESULTS.add(new TripleData(2, 3, 0));
        SET_RESULTS.add(new TripleData(5, 4, 0));
        SET_RESULTS.add(new TripleData(4, 5, 0));
        SET_RESULTS.add(new TripleData(1, 6, 1));
        SET_RESULTS.add(new TripleData(2, 6, 1));
        SET_RESULTS.add(new TripleData(3, 6, 1));
        SET_RESULTS.add(new TripleData(4, 6, 1));
        SET_RESULTS.add(new TripleData(5, 6, 0));
        SET_RESULTS.add(new TripleData(6, 1, -1));
        SET_RESULTS.add(new TripleData(6, 2, -1));
        SET_RESULTS.add(new TripleData(6, 3, -1));
        SET_RESULTS.add(new TripleData(6, 4, -1));
        SET_RESULTS.add(new TripleData(6, 5, 0));

        // Add test results for games.
        GAME_RESULTS.add(new TripleData(0, 0, 0));
        GAME_RESULTS.add(new TripleData(1, 0, 0));
        GAME_RESULTS.add(new TripleData(2, 0, 0));
        GAME_RESULTS.add(new TripleData(3, 0, 0));
        GAME_RESULTS.add(new TripleData(4, 0, -1));
        GAME_RESULTS.add(new TripleData(0, 1, 0));
        GAME_RESULTS.add(new TripleData(0, 2, 0));
        GAME_RESULTS.add(new TripleData(0, 3, 0));
        GAME_RESULTS.add(new TripleData(0, 4, 1));
        GAME_RESULTS.add(new TripleData(1, 1, 0));
        GAME_RESULTS.add(new TripleData(2, 2, 0));
        GAME_RESULTS.add(new TripleData(3, 3, 0));
        GAME_RESULTS.add(new TripleData(4, 4, 0));
        GAME_RESULTS.add(new TripleData(5, 5, 0));
        GAME_RESULTS.add(new TripleData(6, 6, 0));
        GAME_RESULTS.add(new TripleData(7, 7, 0));
        GAME_RESULTS.add(new TripleData(8, 8, 0));
        GAME_RESULTS.add(new TripleData(9, 9, 0));
        GAME_RESULTS.add(new TripleData(10, 10, 0));
        GAME_RESULTS.add(new TripleData(2, 1, 0));
        GAME_RESULTS.add(new TripleData(1, 2, 0));
        GAME_RESULTS.add(new TripleData(3, 1, 0));
        GAME_RESULTS.add(new TripleData(1, 3, 0));
        GAME_RESULTS.add(new TripleData(3, 2, 0));
        GAME_RESULTS.add(new TripleData(2, 3, 0));
        GAME_RESULTS.add(new TripleData(4, 1, -1));
        GAME_RESULTS.add(new TripleData(4, 2, -1));
        GAME_RESULTS.add(new TripleData(4, 3, 0));
        GAME_RESULTS.add(new TripleData(1, 4, 1));
        GAME_RESULTS.add(new TripleData(2, 4, 1));
        GAME_RESULTS.add(new TripleData(3, 4, 0));
        GAME_RESULTS.add(new TripleData(5, 3, -1));
        GAME_RESULTS.add(new TripleData(5, 4, 0));
        GAME_RESULTS.add(new TripleData(6, 4, -1));
        GAME_RESULTS.add(new TripleData(6, 5, 0));
        GAME_RESULTS.add(new TripleData(7, 5, -1));
        GAME_RESULTS.add(new TripleData(7, 6, 0));
        GAME_RESULTS.add(new TripleData(8, 6, -1));
        GAME_RESULTS.add(new TripleData(3, 5, 1));
        GAME_RESULTS.add(new TripleData(4, 5, 0));
        GAME_RESULTS.add(new TripleData(4, 6, 1));
        GAME_RESULTS.add(new TripleData(5, 6, 0));
        GAME_RESULTS.add(new TripleData(5, 7, 1));
        GAME_RESULTS.add(new TripleData(6, 7, 0));
        GAME_RESULTS.add(new TripleData(6, 8, 1));
        GAME_RESULTS.add(new TripleData(7, 8, 0));
        GAME_RESULTS.add(new TripleData(7, 9, 1));

        // Add tiebreak test results.
        TIEBREAK_RESULTS.add(new TripleData(0, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(1, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(2, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(3, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(4, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(5, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(6, 0, 0));
        TIEBREAK_RESULTS.add(new TripleData(7, 0, -1));
        TIEBREAK_RESULTS.add(new TripleData(0, 1, 0));
        TIEBREAK_RESULTS.add(new TripleData(0, 2, 0));
        TIEBREAK_RESULTS.add(new TripleData(0, 3, 0));
        TIEBREAK_RESULTS.add(new TripleData(0, 4, 0));
        TIEBREAK_RESULTS.add(new TripleData(0, 5, 0));
        TIEBREAK_RESULTS.add(new TripleData(0, 6, 0));
        TIEBREAK_RESULTS.add(new TripleData(0, 7, 1));
        TIEBREAK_RESULTS.add(new TripleData(1, 1, 0));
        TIEBREAK_RESULTS.add(new TripleData(2, 2, 0));
        TIEBREAK_RESULTS.add(new TripleData(3, 3, 0));
        TIEBREAK_RESULTS.add(new TripleData(4, 4, 0));
        TIEBREAK_RESULTS.add(new TripleData(5, 5, 0));
        TIEBREAK_RESULTS.add(new TripleData(6, 6, 0));
        TIEBREAK_RESULTS.add(new TripleData(7, 7, 0));
        TIEBREAK_RESULTS.add(new TripleData(8, 8, 0));
        TIEBREAK_RESULTS.add(new TripleData(9, 9, 0));
        TIEBREAK_RESULTS.add(new TripleData(10, 10, 0));
        TIEBREAK_RESULTS.add(new TripleData(1, 2, 0));
        TIEBREAK_RESULTS.add(new TripleData(2, 1, 0));
        TIEBREAK_RESULTS.add(new TripleData(3, 1, 0));
        TIEBREAK_RESULTS.add(new TripleData(1, 3, 0));
        TIEBREAK_RESULTS.add(new TripleData(3, 2, 0));
        TIEBREAK_RESULTS.add(new TripleData(2, 3, 0));
        TIEBREAK_RESULTS.add(new TripleData(5, 4, 0));
        TIEBREAK_RESULTS.add(new TripleData(4, 5, 0));
        TIEBREAK_RESULTS.add(new TripleData(6, 5, 0));
        TIEBREAK_RESULTS.add(new TripleData(5, 6, 0));
        TIEBREAK_RESULTS.add(new TripleData(7, 1, -1));
        TIEBREAK_RESULTS.add(new TripleData(7, 2, -1));
        TIEBREAK_RESULTS.add(new TripleData(7, 3, -1));
        TIEBREAK_RESULTS.add(new TripleData(7, 4, -1));
        TIEBREAK_RESULTS.add(new TripleData(7, 5, -1));
        TIEBREAK_RESULTS.add(new TripleData(7, 6, 0));
        TIEBREAK_RESULTS.add(new TripleData(8, 6, -1));
        TIEBREAK_RESULTS.add(new TripleData(8, 7, 0));
        TIEBREAK_RESULTS.add(new TripleData(9, 7, -1));
        TIEBREAK_RESULTS.add(new TripleData(9, 8, 0));
        TIEBREAK_RESULTS.add(new TripleData(10, 8, -1));
        TIEBREAK_RESULTS.add(new TripleData(10, 9, 0));
        TIEBREAK_RESULTS.add(new TripleData(1, 7, 1));
        TIEBREAK_RESULTS.add(new TripleData(2, 7, 1));
        TIEBREAK_RESULTS.add(new TripleData(3, 7, 1));
        TIEBREAK_RESULTS.add(new TripleData(4, 7, 1));
        TIEBREAK_RESULTS.add(new TripleData(5, 7, 1));
        TIEBREAK_RESULTS.add(new TripleData(6, 7, 0));
        TIEBREAK_RESULTS.add(new TripleData(6, 8, 1));
        TIEBREAK_RESULTS.add(new TripleData(7, 8, 0));
        TIEBREAK_RESULTS.add(new TripleData(7, 9, 1));
        TIEBREAK_RESULTS.add(new TripleData(8, 9, 0));
        TIEBREAK_RESULTS.add(new TripleData(8, 10, 1));
        TIEBREAK_RESULTS.add(new TripleData(9, 10, 0));
    }

    /** */
    @Test
    public void testCorrectMatchScoreResult() {
        for (TripleData match : MATCH_RESULTS) {
            assertEquals(
                match.result(),
                ScoreboardUtil.matchScoreResult(match.one(), match.two())
            );
        }
    }

    /** */
    @Test
    public void testIncorrectMatchScoreResult() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScoreboardUtil.matchScoreResult(-1, 0);
            ScoreboardUtil.matchScoreResult(0, -1);
            ScoreboardUtil.matchScoreResult(-1, -1);
            ScoreboardUtil.matchScoreResult(1, -1);
            ScoreboardUtil.matchScoreResult(2, 2);
            ScoreboardUtil.matchScoreResult(4, 2);
        });
    }

    /** */
    @Test
    public void testCorrectSetScoreResult() {
        for (TripleData set : SET_RESULTS) {
            assertEquals(
                set.result(),
                ScoreboardUtil.setScoreResult(set.one(), set.two())
            );
        }
    }

    /** */
    @Test
    public void testIncorrectSetScoreResult() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScoreboardUtil.setScoreResult(-1, 0);
            ScoreboardUtil.setScoreResult(0, -1);
            ScoreboardUtil.setScoreResult(-1, -1);
        });
    }

    /** */
    @Test
    public void testCorrectGameScoreResult() {
        for (TripleData game : GAME_RESULTS) {
            assertEquals(
                game.result(),
                ScoreboardUtil.gameScoreResult(game.one(), game.two())
            );
        }
    }

    /** */
    @Test
    public void testIncorrectGameScoreResult() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScoreboardUtil.gameScoreResult(-1, 0);
            ScoreboardUtil.gameScoreResult(-1, -1);
            ScoreboardUtil.gameScoreResult(0, -1);
        });
    }

    /** */
    @Test
    public void testCorrectTiebreakScoreResult() {
        for (TripleData tiebreak : TIEBREAK_RESULTS) {
            assertEquals(
                tiebreak.result(),
                ScoreboardUtil.tiebreakScoreResult(
                    tiebreak.one(),
                    tiebreak.two())
            );
        }
    }

    /** */
    @Test
    public void testIncorrectTiebreakScoreResult() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScoreboardUtil.tiebreakScoreResult(-1, 0);
            ScoreboardUtil.tiebreakScoreResult(0, -1);
            ScoreboardUtil.tiebreakScoreResult(-1, -1);
        });
    }
}
