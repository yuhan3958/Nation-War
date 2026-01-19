package com.nationwar.core.server.claim;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the policy for which dimensions are claimable.
 */
public class ClaimPolicyService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<ResourceKey<Level>> claimableDimensions = new HashSet<>();

    public ClaimPolicyService() {
        // In a real implementation, this would be loaded from a config file.
        // For now, we'll use the defaults specified in the PRD.
        claimableDimensions.add(Level.OVERWORLD);
        // Example for Ad Astra dimensions (these would need to be present)
        // claimableDimensions.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "moon")));
        // claimableDimensions.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "mars")));

        LOGGER.info("Initialized ClaimPolicyService. Claimable dimensions: {}", claimableDimensions);
    }

    /**
     * Checks if a dimension is claimable based on the current policy.
     *
     * @param dimensionKey The key of the dimension to check.
     * @return True if the dimension is claimable, false otherwise.
     */
    public boolean isClaimable(ResourceKey<Level> dimensionKey) {
        return claimableDimensions.contains(dimensionKey);
    }

    public void loadConfig(List<String> dimensionIds) {
        claimableDimensions.clear();
        // In a real implementation, convert string IDs from config to ResourceKey<Level>
        // and add them to the set.
        LOGGER.info("Claimable dimensions reloaded from config.");
    }
}
