package com.nationwar.core.server.dynmap;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynmap.forge.DynmapForge;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.Optional;

/**
 * Handles integration with the Dynmap plugin.
 * This class is safe to load even if Dynmap is not present.
 */
public class DynmapBridge {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MARKER_SET_ID = "nation_claims";
    private static final String MARKER_SET_LABEL = "Nation Claims";

    private Optional<MarkerAPI> markerAPI = Optional.empty();
    private Optional<MarkerSet> markerSet = Optional.empty();

    public void initialize() {
        try {
            // This will throw NoClassDefFoundError if Dynmap is not installed.
            // The standard Forge pattern is to get the mod's public static INSTANCE
            // and then call the instance method to get the API.
            MarkerAPI api = DynmapForge.INSTANCE.getMarkerAPI();
            if (api == null) {
                LOGGER.info("Dynmap not found or not enabled. Dynmap integration will be disabled.");
                return;
            }

            this.markerAPI = Optional.of(api);
            LOGGER.info("Dynmap found! Initializing integration.");

            this.markerSet = Optional.ofNullable(markerAPI.get().getMarkerSet(MARKER_SET_ID));
            if (markerSet.isEmpty()) {
                this.markerSet = Optional.of(markerAPI.get().createMarkerSet(MARKER_SET_ID, MARKER_SET_LABEL, null, true));
            }

            if (markerSet.isPresent()) {
                LOGGER.info("Nation claims marker set '{}' is ready.", MARKER_SET_LABEL);
            } else {
                LOGGER.error("Failed to create or retrieve the Dynmap marker set.");
            }

        } catch (NoClassDefFoundError e) {
            LOGGER.info("Dynmap plugin not found. Dynmap integration is disabled.");
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while initializing Dynmap integration.", e);
        }
    }

    /**
     * Updates a claim marker on the Dynmap.
     *
     * @param dimension The dimension of the chunk.
     * @param chunkPos  The position of the chunk.
     * @param nationName The name of the new owner nation, or empty to remove the claim.
     * @param color The color of the claim marker.
     */
    public void updateClaimMarker(ResourceKey<Level> dimension, ChunkPos chunkPos, Optional<String> nationName, int color) {
        if (markerSet.isEmpty()) {
            return; // Dynmap not active
        }

        String worldName = dimension.location().getPath(); // This is a simplification. A config mapping is better.
        String markerId = "claim_" + chunkPos.x + "_" + chunkPos.z;

        // Remove existing marker first
        AreaMarker existingMarker = markerSet.get().findAreaMarker(markerId);
        if (existingMarker != null) {
            existingMarker.deleteMarker();
        }

        // If a new owner exists, create a new marker
        if (nationName.isPresent()) {
            double[] x = new double[4];
            double[] z = new double[4];
            x[0] = chunkPos.getMinBlockX(); z[0] = chunkPos.getMinBlockZ();
            x[1] = chunkPos.getMinBlockX(); z[1] = chunkPos.getMaxBlockZ() + 1;
            x[2] = chunkPos.getMaxBlockX() + 1; z[2] = chunkPos.getMaxBlockZ() + 1;
            x[3] = chunkPos.getMaxBlockX() + 1; z[3] = chunkPos.getMinBlockZ();

            AreaMarker marker = markerSet.get().createAreaMarker(markerId, nationName.get(), false, worldName, x, z, false);
            if (marker != null) {
                marker.setFillStyle(0.5, color);
                marker.setLineStyle(2, 1.0, color);
            }
        }
    }
}
