package com.nationwar.nation.server.manager;

import com.nationwar.nation.common.MemberRank;
import com.nationwar.nation.common.Nation;
import com.nationwar.nation.common.NationMember;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages all nation and member data on the server.
 */
public class NationManager {
    private final Map<UUID, Nation> nations = new ConcurrentHashMap<>();
    private final Map<UUID, NationMember> members = new ConcurrentHashMap<>(); // Player UUID -> Member Info

    /**
     * Creates a new nation with a random initial color.
     *
     * @param name   The name of the new nation.
     * @param leaderId The UUID of the player who will be the leader.
     * @return The newly created Nation object.
     */
    public Nation createNation(String name, UUID leaderId) {
        UUID nationId = UUID.randomUUID();
        // Assign a random, bright color on creation
        int initialColor = 0xFF000000 | ThreadLocalRandom.current().nextInt(0x808080, 0xFFFFFF + 1);
        Nation nation = new Nation(nationId, name, initialColor);
        nations.put(nationId, nation);

        NationMember leader = new NationMember(leaderId, nationId, MemberRank.LEADER);
        members.put(leaderId, leader);

        return nation;
    }

    /**
     * Gets the nation a player belongs to.
     *
     * @param playerId The player's UUID.
     * @return An Optional containing the Nation if the player is in one, otherwise empty.
     */
    public Optional<Nation> getNationOfPlayer(UUID playerId) {
        return Optional.ofNullable(members.get(playerId))
                .map(member -> nations.get(member.nationId()));
    }
    
    /**
     * Gets the membership details of a player.
     *
     * @param playerId The player's UUID.
     * @return An Optional containing the NationMember if the player is in a nation, otherwise empty.
     */
    public Optional<NationMember> getMember(UUID playerId) {
        return Optional.ofNullable(members.get(playerId));
    }

    public boolean isPlayerInNation(UUID playerId) {
        return members.containsKey(playerId);
    }

    public Optional<Nation> getNationByName(String name) {
        return nations.values().stream()
                .filter(n -> n.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
