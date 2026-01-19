package com.nationwar.core.common.guard;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The result of a guard's check.
 * This is an immutable record.
 *
 * @param decision The decision made by the guard (ALLOW, DENY, PASS).
 * @param reasonCode A standardized code representing the reason for the decision (e.g., "not_your_claim").
 * @param messageKey An optional translation key for a user-facing message.
 */
public record GuardResult(GuardDecision decision, @Nullable String reasonCode, @Nullable String messageKey) {

    /**
     * A standard result for passing the decision to the next guard.
     */
    public static final GuardResult PASS = new GuardResult(GuardDecision.PASS, null, null);

    /**
     * A standard result for allowing an action without a specific reason.
     */
    public static final GuardResult ALLOW = new GuardResult(GuardDecision.ALLOW, "allowed", null);

    /**
     * Creates a DENY result with a specific reason and message.
     *
     * @param reasonCode The reason code.
     * @param messageKey The translation key for the message.
     * @return A new GuardResult instance with a DENY decision.
     */
    public static GuardResult deny(String reasonCode, String messageKey) {
        return new GuardResult(GuardDecision.DENY, reasonCode, messageKey);
    }

    public Optional<String> getReasonCode() {
        return Optional.ofNullable(reasonCode);
    }

    public Optional<String> getMessageKey() {
        return Optional.ofNullable(messageKey);
    }
}
