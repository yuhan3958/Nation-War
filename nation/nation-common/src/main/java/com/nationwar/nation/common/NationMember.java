package com.nationwar.nation.common;

import java.util.UUID;

/**
 * Represents a player's membership in a nation.
 *
 * @param playerId The UUID of the player.
 * @param nationId The UUID of the nation they belong to.
 * @param rank     The member's rank within the nation.
 */
public record NationMember(UUID playerId, UUID nationId, MemberRank rank) {
}
