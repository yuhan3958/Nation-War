package com.nationwar.core.client.gui.registry;

import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A central registry for custom screens.
 * Allows other mods to register their screens and open them using a ScreenId.
 */
public final class ScreenRegistry {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<ScreenId, Supplier<Screen>> screenFactories = new ConcurrentHashMap<>();

    private ScreenRegistry() {
        // Prevent instantiation
    }

    /**
     * Registers a new screen factory.
     *
     * @param id      The unique ID for the screen.
     * @param factory A supplier that creates a new instance of the screen.
     */
    public static void register(ScreenId id, Supplier<Screen> factory) {
        if (screenFactories.containsKey(id)) {
            LOGGER.warn("Screen ID '{}' is already registered. Overwriting.", id);
        }
        screenFactories.put(id, factory);
        LOGGER.info("Registered screen with ID '{}'", id);
    }

    /**
     * Creates a new screen instance for the given ID.
     *
     * @param id The ID of the screen to create.
     * @return A new Screen instance, or null if the ID is not registered.
     */
    public static Screen createScreen(ScreenId id) {
        Supplier<Screen> factory = screenFactories.get(id);
        if (factory != null) {
            return factory.get();
        }
        LOGGER.error("Attempted to create an unregistered screen with ID '{}'", id);
        return null;
    }
}
