package io.github.winchest3r.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "Player", indexes = {
    @Index(name = "playerUuid_index", columnList = "playerUuid", unique = true)
})
public class Player {
    /** Player's id is used in database as primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playerId")
    private Long id;

    /** Player's UUID is used in url routing. */
    @Column(
        name = "playerUuid",
        columnDefinition = "uuid default random_uuid()")
    private UUID uuid;

    /** As said. */
    private static final int PLAYER_NAME_MAX_LENGTH = 40;

    /** Player's name. It can't be longer then 40 characters. */
    @Column(
        name = "playerName",
        length = PLAYER_NAME_MAX_LENGTH,
        nullable = false)
    private String name;

    /** Set of player's winner matches. */
    @OneToMany(
        mappedBy = Match_.WINNER,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.REMOVE,
            CascadeType.REFRESH
        },
        orphanRemoval = true)
    private Set<Match> matchesWon;

    /**
     * Get player's id.
     * @return player's id as integer value
     */
    public Long getId() {
        return id;
    }

    /**
     * Set new player's id. You don't need to do this manually, usually.
     * @param newId new player's id
     */
    public void setId(final Long newId) {
        this.id = newId;
    }

    /**
     * Get player's UUID. It is used in routing as link to player.
     * @return player's UUID as {@link java.util.UUID}
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set player's UUID. You don't need to do this manually, usually.
     * @param newUuid new player's UUID as {@link java.util.UUID}
     */
    public void setUuid(final UUID newUuid) {
        this.uuid = newUuid;
    }

    /**
     * Get player's name.
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set new player's name.
     * @param newName new player's name. It can't be longer then 40 characters.
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Get player's won matches.
     * @return Set of matches.
     */
    public Set<Match> getMatchesWon() {
        return this.matchesWon;
    }

    /**
     * Set new player's winner matches.
     * @param newWinnerMatches New player's winner matches.
     */
    public void setMatchesWon(final Set<Match> newWinnerMatches) {
        this.matchesWon = newWinnerMatches;
    }

    /** */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof Player
            && ((Player) other).name.equals(this.name)
            && ((Player) other).uuid.equals(this.uuid);
    }

    /** */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.uuid);
    }
}
