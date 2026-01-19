package com.nationwar.core.common.guard;

/**
 * The decision made by a {@link Guard}.
 */
public enum GuardDecision {
    /**
     * Provisionally allows the action. A later guard with a higher priority can still deny it.
     */
    ALLOW,

    /**
     * Immediately denies the action and stops further processing of guards.
     */
    DENY,

    /**
     * Passes on making a decision, allowing the next guard in the chain to evaluate the action.
     */
    PASS;
}
