package com.nationwar.nation.common;

import java.util.Set;
import java.util.UUID;

/**
 * Represents an alliance between two or more nations.
 *
 * @param id          The unique identifier for the alliance.
 * @param memberNationIds A set of UUIDs for the nations that are part of this alliance.
 */
public record Alliance(UUID id, Set<UUID> memberNationIds) {
}
