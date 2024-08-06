package io.github.winchest3r.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "Playset", indexes = {
    @Index(
        name = "playsetUuid_index",
        columnList = "playsetUuid",
        unique = true
    )
})
public class PlaySet {
    /** Set id that is related to database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playsetId")
    private Long id;

    /** Match that is connected to the set. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "matchId",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "PlaysetMatchForeignKey",
            foreignKeyDefinition =
                "FOREIGN KEY (matchId) "
                + "REFERENCES Match (matchId) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"))
    private Match match;

    /** Playset UUID. */
    @Column(
        name = "playsetUuid",
        columnDefinition = "uuid default random_uuid()"
    )
    private UUID uuid;

    /** Datetime with timezone to indicate set beginning. */
    @Column(
        name = "startTimestamp",
        columnDefinition =
            "timestamp(6) with time zone default current_timestamp()")
    private OffsetDateTime startTime;

    /** First player set score. */
    @Column(name = "playerOneSetScore", columnDefinition = "integer default 0")
    private Integer playerOneSetScore;

    /** Second player set score. */
    @Column(name = "playerTwoSetScore", columnDefinition = "integer default 0")
    private Integer playerTwoSetScore;

    /** Games related to the set. */
    @OneToMany(
        mappedBy = Game_.PLAYSET,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.REMOVE,
            CascadeType.REFRESH
        },
        orphanRemoval = true)
    private Set<Game> games;

    /**
     * Get set id.
     * @return Id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set new id.
     * @param newId New id.
     */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /**
     * Get playset UUID.
     * @return Playset UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Set new playset UUID.
     * @param newUuid New playset UUID.
     */
    public void setUuid(final UUID newUuid) {
        this.uuid = newUuid;
    }

    /**
     * Get match.
     * @return Match.
     */
    public Match getMatch() {
        return this.match;
    }

    /**
     * Set new match.
     * @param newMatch New match.
     */
    public void setMatch(final Match newMatch) {
        this.match = newMatch;
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
     * Get set score for player one.
     * @return Set score for player one.
     */
    public Integer getPlayerOneSetScore() {
        return this.playerOneSetScore;
    }

    /**
     * Set new set score for player one.
     * @param newPlayerOneSetScore Set score for player one.
     */
    public void setPlayerOneSetScore(final Integer newPlayerOneSetScore) {
        this.playerOneSetScore = newPlayerOneSetScore;
    }

    /**
     * Get set score for player two.
     * @return Set score for player two.
     */
    public Integer getPlayerTwoSetScore() {
        return this.playerTwoSetScore;
    }

    /**
     * Set new set score for player two.
     * @param newPlayerTwoSetScore New set score for player two.
     */
    public void setPlayerTwoSetScore(final Integer newPlayerTwoSetScore) {
        this.playerTwoSetScore = newPlayerTwoSetScore;
    }

    /**
     * Get games related to the set.
     * @return Games related to the set.
     */
    public Set<Game> getGames() {
        return this.games;
    }

    /**
     * Set new games related to the set.
     * @param newGames New games related to the set.
     */
    public void setGames(final Set<Game> newGames) {
        this.games = newGames;
    }

    /** */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof PlaySet
            && ((PlaySet) other).uuid.equals(this.uuid);
    }

    /** */
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
