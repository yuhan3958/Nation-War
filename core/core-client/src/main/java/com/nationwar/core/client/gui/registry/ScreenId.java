package com.nationwar.core.client.gui.registry;

import net.minecraft.resources.ResourceLocation;

/**
 * A unique identifier for a screen, wrapping a ResourceLocation.
 *
 * @param id The ResourceLocation identifying the screen.
 */
public record ScreenId(ResourceLocation id) {
    public static ScreenId of(String namespace, String path) {
        return new ScreenId(new ResourceLocation(namespace, path));
    }
}
