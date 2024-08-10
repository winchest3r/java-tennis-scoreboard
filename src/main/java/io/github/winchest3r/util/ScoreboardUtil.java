package io.github.winchest3r.util;

public final class ScoreboardUtil {
    private ScoreboardUtil() { }

    /**
     * Get result of match using score parameters.
     * Possible states: 0-0, 0-1, 1-0, 1-1, 1-2, 2-1.
     * @param playerOneScore can be 0, 1 or 2.
     * @param playerTwoScore can be 0, 1 or 2.
     * @return -1 if playerOne won,
     * 1 if playerTwo won and 0 if match is ongoing.
     * @throws IllegalArgumentException if one of the scores is negative.
     * or more then 2.
     */
    public static int matchScoreResult(
            final int playerOneScore,
            final int playerTwoScore) {
        if (playerOneScore < 0 || playerTwoScore < 0) {
            throw new IllegalArgumentException("Match score can't be negative");
        }
        if (playerOneScore > 2 || playerTwoScore > 2) {
            throw new IllegalArgumentException(
                "Match score can't be more then 2"
            );
        }
        if (playerOneScore == 2 && playerTwoScore == 2) {
            throw new IllegalArgumentException("Both scores can't be equal 2");
        }
        if (playerOneScore > 1) {
            return -1;
        }
        if (playerTwoScore > 1) {
            return 1;
        }
        return 0;
    }

    /**
     * Get result of set using score parameters.
     * Possible states are the same as tennis set rules.
     * @param playerOneScore Positive value.
     * @param playerTwoScore Positive value.
     * @return -1 if playerOne won, 1 if playerTwo won
     * and 0 if match is ongoing.
     * @throws IllegalArgumentException if one of the scores is negative.
     */
    public static int setScoreResult(
            final int playerOneScore,
            final int playerTwoScore) {
        if (playerOneScore < 0 || playerTwoScore < 0) {
            throw new IllegalArgumentException("Set score can't be negative");
        }
        final int sixScore = 6;
        final int sevenScore = 7;
        // If diff >= 2 and player One get 6 points.
        if (playerOneScore >= sixScore
                && playerOneScore - playerTwoScore >= 2) {
            return -1;
        }
        // If diff >= 2 and player Two get 6 points.
        if (playerTwoScore >= sixScore
                && playerTwoScore - playerOneScore >= 2) {
            return 1;
        }
        // Tiebreaker.
        if (playerOneScore == sevenScore && playerTwoScore == sixScore
                || playerOneScore == sixScore && playerTwoScore == sevenScore) {
            return playerOneScore == sevenScore ? -1 : 1;
        }
        return 0;
    }

    /**
     * Get result of game using score parameters.
     * Possible states are the same as tennis game rules.
     * Scores: 0 is 0, 1 is 15, 2 is 30, 3 is 40, 4 - game, ...
     * @param playerOneScore Positive value.
     * @param playerTwoScore Positive value.
     * @return -1 if player One won, 1 if player Two won
     * and 0 if match is ongoing.
     * @throws IllegalArgumentException if one of the scores is negative.
     */
    public static int gameScoreResult(
            final int playerOneScore,
            final int playerTwoScore) {
        if (playerOneScore < 0 || playerTwoScore < 0) {
            throw new IllegalArgumentException("Game score can't be negative");
        }
        final int gameScore = 4;
        // If diff >= 2 and player One get game.
        if (playerOneScore >= gameScore
                && playerOneScore - playerTwoScore >= 2) {
            return -1;
        }
        // If diff >= 2 and player Two get game.
        if (playerTwoScore >= gameScore
                && playerTwoScore - playerOneScore >= 2) {
            return 1;
        }
        return 0;
    }

    /**
     * Get result of tiebreak using score parameters.
     * Possible states are the same as tennis game rules.
     * @param playerOneScore Positive value.
     * @param playerTwoScore Positive value.
     * @return -1 if player One won, 1 if player Two won
     * and 0 if match is ongoing.
     */
    public static int tiebreakScoreResult(
            final int playerOneScore,
            final int playerTwoScore) {
        if (playerOneScore < 0 || playerTwoScore < 0) {
            throw new IllegalArgumentException(
                "Tiebreak score can't be negative"
            );
        }
        final int tiebreakScore = 7;
        // If diff >= 2 and player One get 7.
        if (playerOneScore >= tiebreakScore
                && playerOneScore - playerTwoScore >= 2) {
            return -1;
        }
        // If diff >= 2 and player Tow get 7.
        if (playerTwoScore >= tiebreakScore
                && playerTwoScore - playerOneScore >= 2) {
            return 1;
        }
        return 0;
    }
}
