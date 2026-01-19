package com.nationwar.core.common.event;

import com.nationwar.core.common.context.ActionContext;
import com.nationwar.core.common.guard.GuardResult;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired on the Forge event bus when the GuardBus denies an action.
 * It contains the context of the action and the result from the guard that denied it.
 */
public class ActionDeniedEvent extends Event {
    private final ActionContext context;
    private final GuardResult guardResult;

    public ActionDeniedEvent(ActionContext context, GuardResult result) {
        this.context = context;
        this.guardResult = result;
    }

    public ActionContext getContext() {
        return context;
    }

    /**
     * Returns the GuardResult from the guard that denied the action.
     * Renamed from getResult() to avoid conflict with {@link Event#getResult()}.
     *
     * @return The GuardResult containing the denial reason.
     */
    public GuardResult getGuardResult() {
        return guardResult;
    }
}
