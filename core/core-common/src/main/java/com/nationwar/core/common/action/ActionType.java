package com.nationwar.core.common.action;

/**
 * Represents the type of action being performed in the game world.
 * This is used by the GuardBus to determine which guards should be checked.
 */
public enum ActionType {
    /**
     * When a block is broken by a player or a machine.
     */
    BLOCK_BREAK,

    /**
     * When a block is placed by a player or a machine.
     */
    BLOCK_PLACE,

    /**
     * When a player right-clicks a block (e.g., lever, button, door).
     */
    BLOCK_USE,

    /**
     * When a player attempts to open a container (e.g., chest, furnace).
     */
    CONTAINER_OPEN,

    /**
     * When an entity is damaged by another entity.
     */
    ENTITY_DAMAGE,

    /**
     * When a projectile (e.g., arrow, fireball) impacts a block or entity.
     */
    PROJECTILE_IMPACT,

    /**
     * When a block is damaged or destroyed by an explosion.
     */
    EXPLOSION_BLOCK_DAMAGE,

    /**
     * When a fluid is placed or used in the world.
     */
    FLUID_USE,

    /**
     * When fire is ignited.
     */
    FIRE_IGNITE,

    /**
     * When an item is crafted.
     */
    ITEM_CRAFT,

    /**
     * When an item is smelted in a furnace or similar block.
     */
    ITEM_SMELT,

    /**
     * When a machine finishes processing and outputs an item.
     * This is particularly for the Tech mod to enforce progression.
     */
    MACHINE_OUTPUT;
}
