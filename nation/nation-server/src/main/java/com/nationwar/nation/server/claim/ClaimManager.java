package com.nationwar.nation.server.claim;

import com.nationwar.nation.common.Nation;
import com.nationwar.nation.common.event.ChunkClaimChangedEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Manages chunk claims for all nations across all dimensions.
 * This class is thread-safe.
 */
public class ClaimManager {
    // Dimension -> ChunkPos -> Nation UUID
    private final Map<ResourceKey<Level>, Map<ChunkPos, UUID>> claims = new ConcurrentHashMap<>();

    /**
     * Claims a chunk for a nation.
     *
     * @param nation     The nation claiming the chunk.
     * @param levelKey   The dimension the chunk is in.
     * @param chunkPos   The position of the chunk.
     * @return True if the chunk was successfully claimed, false if it was already claimed.
     */
    public boolean claimChunk(Nation nation, ResourceKey<Level> levelKey, ChunkPos chunkPos) {
        Map<ChunkPos, UUID> dimensionClaims = claims.computeIfAbsent(levelKey, k -> new ConcurrentHashMap<>());
        boolean success = dimensionClaims.putIfAbsent(chunkPos, nation.getId()) == null;
        if (success) {
            MinecraftForge.EVENT_BUS.post(new ChunkClaimChangedEvent(chunkPos, levelKey, Optional.of(nation)));
        }
        return success;
    }

    /**
     * Unclaims a chunk.
     *
     * @param levelKey The dimension the chunk is in.
     * @param chunkPos The position of the chunk.
     */
    public void unclaimChunk(ResourceKey<Level> levelKey, ChunkPos chunkPos) {
        Map<ChunkPos, UUID> dimensionClaims = claims.get(levelKey);
        if (dimensionClaims != null) {
            if (dimensionClaims.remove(chunkPos) != null) {
                MinecraftForge.EVENT_BUS.post(new ChunkClaimChangedEvent(chunkPos, levelKey, Optional.empty()));
            }
        }
    }

    /**
     * Gets the UUID of the nation that owns the specified chunk.
     *
     * @param levelKey The dimension the chunk is in.
     * @param chunkPos The position of the chunk.
     * @return An Optional containing the nation's UUID if the chunk is claimed, otherwise empty.
     */
    public Optional<UUID> getNationAt(ResourceKey<Level> levelKey, ChunkPos chunkPos) {
        return Optional.ofNullable(claims.getOrDefault(levelKey, Map.of()).get(chunkPos));
    }

    /**
     * Gets all claims for a specific nation.
     *
     * @param nationId The UUID of the nation.
     * @return A stream of ClaimInfo records.
     */
    public Stream<ClaimInfo> getClaimsForNation(UUID nationId) {
        return claims.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream()
                        .filter(chunkEntry -> chunkEntry.getValue().equals(nationId))
                        .map(chunkEntry -> new ClaimInfo(entry.getKey(), chunkEntry.getKey())));
    }

    public record ClaimInfo(ResourceKey<Level> dimension, ChunkPos chunkPos) {}
}
