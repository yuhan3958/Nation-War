package com.nationwar.core.common.guard;

import com.nationwar.core.common.context.ActionContext;

/**
 * A Guard is a component that can check whether an action is allowed.
 * Guards are registered with the {@link GuardBus} and are executed in order of priority.
 */
@FunctionalInterface
public interface Guard {

    /**
     * Checks if the given action is allowed.
     *
     * @param context The context of the action being performed.
     * @return A {@link GuardResult} indicating whether the action is allowed, denied, or passed.
     */
    GuardResult check(ActionContext context);

    /**
     * The priority of this guard. Guards with higher numbers are executed first.
     * The default priority is 0.
     * <p>
     * Recommended priority ranges:
     * <ul>
     *     <li>1000+: System-level guards (e.g., server protection)</li>
     *     <li>500-999: High-priority guards (e.g., War/Admin overrides)</li>
     *     <li>0-499: Standard guards (e.g., Nation claims, Tech permissions)</li>
     *     <li><0: Low-priority guards (e.g., logging, default behaviors)</li>
     * </ul>
     *
     * @return The priority of this guard.
     */
    default int getPriority() {
        return 0;
    }
}
