package io.github.winchest3r.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "match", indexes = {
    @Index(name = "matchUuid_index", columnList = "matchUuid", unique = true)
})
public class Match {
    /** Match id is used in database as primary key. */
    @Id
    private int id;

    /** Match UUID is used in url routing. */
    @Column(
        name = "matchUuid",
        columnDefinition = "uuid default random_uuid()")
    private UUID uuid;

    /** Winner player or null if match is ongoing. */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @MapsId
    private Player winner;
}
