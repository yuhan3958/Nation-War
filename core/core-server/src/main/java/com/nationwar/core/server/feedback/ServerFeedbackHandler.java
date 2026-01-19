package com.nationwar.core.server.feedback;

import com.nationwar.core.common.context.Actor;
import com.nationwar.core.common.event.ActionDeniedEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles server-side feedback and logging when an action is denied.
 * This class must be registered on the Forge event bus to function.
 */
public class ServerFeedbackHandler {

    private static final Logger AUDIT_LOGGER = LogManager.getLogger("NationWarAudit");
    private final MinecraftServer server;

    public ServerFeedbackHandler(MinecraftServer server) {
        this.server = server;
    }

    @SubscribeEvent
    public void onActionDenied(ActionDeniedEvent event) {
        Actor actor = event.getContext().getActor();
        String reason = event.getGuardResult().getReasonCode().orElse("unknown_reason");
        String messageKey = event.getGuardResult().getMessageKey().orElse("nationwar.feedback.denied.default");

        // --- Audit Logging ---
        AUDIT_LOGGER.info(
                "Action Denied | Type: {} | Actor: {} ({}) | Location: {} @ {} | Reason: {}",
                event.getContext().getActionType(),
                actor.name(),
                actor.id(),
                event.getContext().getLocation().position().toShortString(),
                event.getContext().getLocation().dimension().location(),
                reason
        );

        // --- Player Feedback ---
        if ("player".equals(actor.type())) {
            ServerPlayer player = server.getPlayerList().getPlayer(actor.id());
            if (player != null) {
                // In a real implementation, you'd use a translation key.
                // For now, we send a direct component.
                player.sendSystemMessage(Component.literal("Action denied: " + reason), true); // true = use action bar
            }
        }
    }
}
