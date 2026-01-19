package com.nationwar.core.common.guard;

import com.nationwar.core.common.context.ActionContext;
import com.nationwar.core.common.event.ActionDeniedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A central bus for registering and processing guards.
 * This class provides a static interface for checking actions against all registered guards.
 */
public final class GuardBus {

    private static final List<Guard> guards = new CopyOnWriteArrayList<>();
    private static volatile boolean sorted = false;

    private GuardBus() {
        // Prevent instantiation
    }

    /**
     * Registers a guard to the bus.
     * This method is thread-safe.
     *
     * @param guard The guard to register.
     */
    public static void register(Guard guard) {
        guards.add(guard);
        sorted = false; // Mark the list as unsorted whenever a new guard is added.
    }

    /**
     * Checks the given action against all registered guards.
     * The guards are executed in descending order of priority.
     * <p>
     * The logic is as follows:
     * 1. Any guard returning {@link GuardDecision#DENY} immediately stops the process, fires an {@link ActionDeniedEvent}, and the action is denied.
     * 2. If any guards return {@link GuardDecision#ALLOW}, the action is provisionally allowed. The highest priority {@code ALLOW} is recorded.
     * 3. If all guards return {@link GuardDecision#PASS}, the action is passed.
     *
     * @param context The context of the action to check.
     * @return The final {@link GuardResult} after processing all relevant guards.
     */
    public static GuardResult check(ActionContext context) {
        ensureSorted();

        GuardResult firstAllowResult = null;

        for (Guard guard : guards) {
            GuardResult result = guard.check(context);
            GuardDecision decision = result.decision();

            if (decision == GuardDecision.DENY) {
                // DENY is an immediate veto. Fire an event for listeners.
                MinecraftForge.EVENT_BUS.post(new ActionDeniedEvent(context, result));
                return result;
            }

            if (decision == GuardDecision.ALLOW && firstAllowResult == null) {
                // Record the first ALLOW from the highest-priority guard, but continue checking for DENYs.
                firstAllowResult = result;
            }
            // If decision is PASS, do nothing and continue to the next guard.
        }

        if (firstAllowResult != null) {
            return firstAllowResult;
        }

        // If we get here, no guard denied and no guard allowed, so we pass.
        return GuardResult.PASS;
    }

    private static void ensureSorted() {
        if (!sorted) {
            synchronized (GuardBus.class) {
                if (!sorted) {
                    guards.sort(Comparator.comparingInt(Guard::getPriority).reversed());
                    sorted = true;
                }
            }
        }
    }
}
