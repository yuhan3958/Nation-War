package com.nationwar.nation.server.guard;

import com.nationwar.core.common.action.ActionType;
import com.nationwar.core.common.context.ActionContext;
import com.nationwar.core.common.guard.Guard;
import com.nationwar.core.common.guard.GuardResult;
import com.nationwar.nation.server.claim.ClaimManager;
import com.nationwar.nation.server.manager.NationManager;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * A guard that protects claimed chunks from unauthorized actions.
 */
public class NationGuard implements Guard {

    private final ClaimManager claimManager;
    private final NationManager nationManager;

    // Actions that are protected by claim ownership.
    private static final Set<ActionType> PROTECTED_ACTIONS = Set.of(
            ActionType.BLOCK_BREAK,
            ActionType.BLOCK_PLACE,
            ActionType.BLOCK_USE,
            ActionType.CONTAINER_OPEN,
            ActionType.FLUID_USE,
            ActionType.FIRE_IGNITE
    );

    public NationGuard(ClaimManager claimManager, NationManager nationManager) {
        this.claimManager = claimManager;
        this.nationManager = nationManager;
    }

    @Override
    public GuardResult check(ActionContext context) {
        // This guard only cares about protected action types.
        if (!PROTECTED_ACTIONS.contains(context.getActionType())) {
            return GuardResult.PASS;
        }

        ChunkPos chunkPos = new ChunkPos(context.getLocation().position());
        Optional<UUID> ownerNationIdOpt = claimManager.getNationAt(context.getLocation().dimension(), chunkPos);

        // If the chunk is not claimed, this guard doesn't care.
        if (ownerNationIdOpt.isEmpty()) {
            return GuardResult.PASS;
        }

        UUID ownerNationId = ownerNationIdOpt.get();
        Optional<UUID> actorNationIdOpt = nationManager.getNationOfPlayer(context.getActor().id()).map(n -> n.getId());

        // If the actor is not in a nation, or is in a different nation.
        if (actorNationIdOpt.isEmpty() || !ownerNationId.equals(actorNationIdOpt.get())) {
            // --- WAR & ALLIANCE LOGIC would go here ---

            // If none of the exceptions apply, deny the action.
            return GuardResult.deny("not_your_claim", "nation.feedback.denied.claim");
        }

        // The actor is in the owner nation, so allow the action.
        return GuardResult.PASS;
    }

    @Override
    public int getPriority() {
        // Standard priority for claim checks.
        return 100;
    }
}
