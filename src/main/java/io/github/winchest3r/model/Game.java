package io.github.winchest3r.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "Game", indexes = {
    @Index(name = "gameUuid_index", columnList = "gameUuid", unique = true)
})
public class Game {
    /** Game id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameId")
    private Long id;

    /** Game UUID. */
    @Column(
        name = "gameUuid",
        columnDefinition = "uuid default random_uuid()")
    @Generated
    private UUID uuid;

    /** Playset related to the game. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "playsetId",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "GamePlaysetForeignKey",
            foreignKeyDefinition =
                "FOREIGN KEY (playsetId) "
                + "REFERENCES Playset (playsetId) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"))
    private Playset playset;

    /** Time of game starting. */
    @Column(
        name = "startTimestamp",
        columnDefinition =
            "timestamp(6) with time zone default current_timestamp()")
    private OffsetDateTime startTime;

    /** Player one game score. */
    @Column(name = "playerOneGameScore", columnDefinition = "integer default 0")
    private Integer playerOneGameScore;

    /** Player two game score. */
    @Column(name = "playerTwoGameScore", columnDefinition = "integer default 0")
    private Integer playerTwoGameScore;

    /**
     * Get game id.
     * @return Game id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set game id.
     * @param newId New game id.
     */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /**
     * Get game UUID.
     * @return Game UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Set new game UUID.
     * @param newUuid New game UUID.
     */
    public void setUuid(final UUID newUuid) {
        this.uuid = newUuid;
    }

    /**
     * Get playset related to the game.
     * @return Playset related to the game.
     */
    public Playset getPlayset() {
        return this.playset;
    }

    /**
     * Set new playset related to the game.
     * @param newPlayset New playset related to the game.
     */
    public void setPlayset(final Playset newPlayset) {
        this.playset = newPlayset;
    }

    /**
     * Get start datetime with timezone.
     * @return Start time.
     */
    public OffsetDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * Set new start time.
     * @param newStartTime New start time.
     */
    public void setStartTime(final OffsetDateTime newStartTime) {
        this.startTime = newStartTime;
    }

    /**
     * Get game score for player one.
     * @return Set score for player one.
     */
    public Integer getPlayerOneGameScore() {
        return this.playerOneGameScore;
    }

    /**
     * Set new game score for player one.
     * @param newPlayerOneGameScore Game score for player one.
     */
    public void setPlayerOneGameScore(final Integer newPlayerOneGameScore) {
        this.playerOneGameScore = newPlayerOneGameScore;
    }

    /**
     * Get game score for player two.
     * @return Game score for player two.
     */
    public Integer getPlayerTwoGameScore() {
        return this.playerTwoGameScore;
    }

    /**
     * Set new game score for player two.
     * @param newPlayerTwoGameScore New game score for player two.
     */
    public void setPlayerTwoGameScore(final Integer newPlayerTwoGameScore) {
        this.playerTwoGameScore = newPlayerTwoGameScore;
    }

    /** */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof Playset
            && ((Game) other).uuid.equals(this.uuid);
    }

    /** */
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
