package io.github.winchest3r.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "Match", indexes = {
    @Index(name = "matchUuid_index", columnList = "matchUuid", unique = true)
})
public class Match {
    /** Match id is used in database as primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchId")
    private Long id;

    /** Match UUID is used in url routing. */
    @Column(
        name = "matchUuid",
        columnDefinition = "uuid default random_uuid()")
    @Generated
    private UUID uuid;

    /** Player associated with first player of the set. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "playerOneId",
        referencedColumnName = "playerId",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "MatchPlayerOneForeignKey",
            foreignKeyDefinition =
                "FOREIGN KEY (playerOneId) "
                + "REFERENCES Player (playerId) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"))
    private Player playerOne;

    /** Player associated with second player of the set. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "playerTwoId",
        referencedColumnName = "playerId",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "MatchPlayerTwoForeignKey",
            foreignKeyDefinition =
                "FOREIGN KEY (playerTwoId) "
                + "REFERENCES Player (playerId) "
                + "ON DELETE CASCADE ON UPDATE CASCADE"))
    private Player playerTwo;

    /** Winner player or null if match is ongoing. */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
        name = "winnerId",
        referencedColumnName = "playerId",
        foreignKey = @ForeignKey(name = "MatchWinnerForeignKey")
    )
    private Player winner;

    /** Sets that are associated with the match. */
    @OneToMany(
        mappedBy = Playset_.MATCH,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.REMOVE,
            CascadeType.REFRESH
        },
        orphanRemoval = true)
    private Set<Playset> playsets;

    /**
     * Get match id.
     * @return Match id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set match id.
     * @param newId New match id.
     */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /**
     * Get match UUID.
     * @return Match UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Set new match UUID.
     * @param newUuid New match UUID.
     */
    public void setUuid(final UUID newUuid) {
        this.uuid = newUuid;
    }

    /**
     * Get sets associated with the match.
     * @return Set of sets.
     */
    public java.util.Set<Playset> getPlaysets() {
        return this.playsets;
    }

    /**
     * Set new sets that is associated with the match.
     * @param newPlaysets New set of sets.
     */
    public void setPlaysets(final Set<Playset> newPlaysets) {
        this.playsets = newPlaysets;
    }

    /**
     * Get player one.
     * @return Player one.
     */
    public Player getPlayerOne() {
        return this.playerOne;
    }

    /**
     * Set new player one.
     * @param newPlayerOne New player one.
     */
    public void setPlayerOne(final Player newPlayerOne) {
        this.playerOne = newPlayerOne;
    }

    /**
     * Get player two.
     * @return Player two.
     */
    public Player getPlayerTwo() {
        return this.playerTwo;
    }

    /**
     * Set new player two.
     * @param newPlayerTwo New player two.
     */
    public void setPlayerTwo(final Player newPlayerTwo) {
        this.playerTwo = newPlayerTwo;
    }

    /**
     * Get winner player or null if match is ongoing.
     * @return Winner player or null.
     */
    public Player getWinner() {
        return this.winner;
    }

    /**
     * Set winner player.
     * @param player Player
     */
    public void setWinner(final Player player) {
        this.winner = player;
    }

    /** */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof Match
            && ((Match) other).uuid.equals(this.uuid);
    }

    /** */
    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    /** */
    @Override
    public String toString() {
        return "[Match " + playerOne + " vs " + playerTwo + " : " + uuid + "]";
    }
}
