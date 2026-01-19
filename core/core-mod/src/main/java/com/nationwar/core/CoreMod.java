package com.nationwar.core;

import com.nationwar.core.server.claim.ClaimPolicyService;
import com.nationwar.core.server.dynmap.DynmapBridge;
import com.nationwar.core.server.feedback.ServerFeedbackHandler;
import com.nationwar.nation.common.Nation;
import com.nationwar.nation.common.event.ChunkClaimChangedEvent;
import com.nationwar.nation.common.event.NationUpdateEvent;
import com.nationwar.nation.server.claim.ClaimManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Mod("core")
public class CoreMod {
    private static final Logger LOGGER = LogManager.getLogger();

    private static ClaimPolicyService claimPolicyService;
    private static DynmapBridge dynmapBridge;
    private static ServerFeedbackHandler serverFeedbackHandler;

    public CoreMod() {
        // Register this mod class to the Forge event bus to receive events like ServerStartingEvent
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("CoreMod initialized.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("CoreMod: Server is starting, initializing server-side services.");

        // Initialize services
        claimPolicyService = new ClaimPolicyService();
        dynmapBridge = new DynmapBridge();
        serverFeedbackHandler = new ServerFeedbackHandler(event.getServer());

        // Initialize Dynmap bridge
        dynmapBridge.initialize();

        // Register the feedback handler to the event bus so it can receive ActionDeniedEvent
        MinecraftForge.EVENT_BUS.register(serverFeedbackHandler);

        LOGGER.info("CoreMod: Server-side services initialized.");
    }

    @SubscribeEvent
    public void onChunkClaimChanged(ChunkClaimChangedEvent event) {
        if (dynmapBridge != null) {
            dynmapBridge.updateClaimMarker(
                    event.getDimension(),
                    event.getChunkPos(),
                    event.getNewOwner().map(Nation::getName),
                    event.getNewOwner().map(Nation::getColor).orElse(0)
            );
        }
    }

    @SubscribeEvent
    public void onNationUpdate(NationUpdateEvent event) {
        if (dynmapBridge != null) {
            // This is a simple way to do it. A more robust service locator might be better later.
            ClaimManager claimManager = com.nationwar.nation.NationMod.getClaimManager();
            if (claimManager != null) {
                Nation nation = event.getNation();
                claimManager.getClaimsForNation(nation.getId()).forEach(claimInfo -> {
                    dynmapBridge.updateClaimMarker(
                            claimInfo.dimension(),
                            claimInfo.chunkPos(),
                            Optional.of(nation.getName()),
                            nation.getColor()
                    );
                });
            }
        }
    }

    public static ClaimPolicyService getClaimPolicyService() {
        return claimPolicyService;
    }
}
