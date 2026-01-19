package com.nationwar.core.common.context;

import java.util.UUID;

/**
 * Represents the entity or system performing an action.
 * This is an immutable record.
 *
 * @param id   The unique identifier of the actor (e.g., Player UUID).
 * @param name The display name of the actor.
 * @param type The type of actor (e.g., "player", "machine").
 */
public record Actor(UUID id, String name, String type) {
    /**
     * A constant representing an unknown or anonymous actor.
     */
    public static final Actor UNKNOWN = new Actor(new UUID(0, 0), "Unknown", "unknown");
}
