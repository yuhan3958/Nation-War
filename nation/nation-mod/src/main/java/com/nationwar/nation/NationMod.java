package com.nationwar.nation;

import com.nationwar.core.common.guard.GuardBus;
import com.nationwar.core.server.claim.ClaimPolicyService;
import com.nationwar.nation.server.claim.ClaimManager;
import com.nationwar.nation.server.command.NationCommand;
import com.nationwar.nation.server.guard.NationGuard;
import com.nationwar.nation.server.manager.NationManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("nation")
public class NationMod {
    private static final Logger LOGGER = LogManager.getLogger();

    private static NationManager nationManager;
    private static ClaimManager claimManager;

    public NationMod() {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("NationMod initialized.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("NationMod: Server is starting, initializing services.");

        // Initialize managers
        nationManager = new NationManager();
        claimManager = new ClaimManager();

        // Register guards
        NationGuard nationGuard = new NationGuard(claimManager, nationManager);
        GuardBus.register(nationGuard);

        LOGGER.info("NationMod: Services and guards initialized and registered.");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // We need to get the ClaimPolicyService from the CoreMod.
        // This is a simple way to do it, but a more robust service locator might be better later.
        ClaimPolicyService claimPolicyService = com.nationwar.core.CoreMod.getClaimPolicyService();
        if (claimPolicyService != null) {
            NationCommand.register(event.getDispatcher(), nationManager, claimManager, claimPolicyService);
            LOGGER.info("NationMod: Commands registered.");
        } else {
            LOGGER.error("NationMod: Could not register commands because ClaimPolicyService was not available.");
        }
    }

    public static NationManager getNationManager() {
        return nationManager;
    }

    public static ClaimManager getClaimManager() {
        return claimManager;
    }
}
