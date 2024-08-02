package io.github.winchest3r.models;

import java.util.UUID;

public class Player {
    /** Player's id is used in database as primary key. */
    private  int id;

    /** Player's UUID is used in url routing. */
    private UUID uuid;

    /** Player's name. It can't be longer then 40 characters. */
    private String name;

    /** Default constructor. */
    public Player() { }

    /**
     * Constructor to initialize all fields.
     * @param newId new player's id
     * @param newUuid new player's UUID as {@link java.util.UUID}
     * @param newName new player's name. It can't be longer then 40 characters.
     */
    public Player(final int newId, final UUID newUuid, final String newName) {
        this.id = newId;
        this.uuid = newUuid;
        this.name = newName;
    }

    /**
     * Get player's id.
     * @return player's id as integer value
     */
    public int getId() {
        return id;
    }

    /**
     * Set new player's id. You don't need to do this manually, usually.
     * @param newId new player's id
     */
    public void setId(final int newId) {
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
}
