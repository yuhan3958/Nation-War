package com.nationwar.core.common.context;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * Represents a specific location in the game world, including the dimension and coordinates.
 * This is an immutable record.
 *
 * @param dimension The key for the dimension (e.g., Overworld, Nether).
 * @param position  The block position within the dimension.
 */
public record Location(ResourceKey<Level> dimension, BlockPos position) {
}
