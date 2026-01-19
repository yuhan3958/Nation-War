package com.nationwar.nation.common.event;

import com.nationwar.nation.common.Nation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.Optional;

/**
 * Fired on the Forge event bus when a chunk's ownership changes.
 */
public class ChunkClaimChangedEvent extends Event {
    private final ChunkPos chunkPos;
    private final ResourceKey<Level> dimension;
    private final Optional<Nation> newOwner;

    public ChunkClaimChangedEvent(ChunkPos chunkPos, ResourceKey<Level> dimension, Optional<Nation> newOwner) {
        this.chunkPos = chunkPos;
        this.dimension = dimension;
        this.newOwner = newOwner;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    /**
     * @return The new owner of the chunk. If empty, the chunk is now unclaimed.
     */
    public Optional<Nation> getNewOwner() {
        return newOwner;
    }
}
