package com.nationwar.core.common.context;

import com.nationwar.core.common.action.ActionType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import java.util.Optional;

/**
 * Provides the full context for an action being performed.
 * Instances of this class are created using the {@link Builder}.
 */
public final class ActionContext {

    private final ActionType actionType;
    private final Actor actor;
    private final Location location;
    private final BlockState blockState;
    private final ItemStack itemInvolved;

    private ActionContext(Builder builder) {
        this.actionType = builder.actionType;
        this.actor = builder.actor;
        this.location = builder.location;
        this.blockState = builder.blockState;
        this.itemInvolved = builder.itemInvolved;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Actor getActor() {
        return actor;
    }

    public Location getLocation() {
        return location;
    }

    public Optional<BlockState> getBlockState() {
        return Optional.ofNullable(blockState);
    }

    public Optional<ItemStack> getItemInvolved() {
        return Optional.ofNullable(itemInvolved);
    }

    public static class Builder {
        private final ActionType actionType;
        private final Location location;
        private Actor actor = Actor.UNKNOWN;
        @Nullable
        private BlockState blockState;
        @Nullable
        private ItemStack itemInvolved;

        public Builder(ActionType actionType, Location location) {
            this.actionType = actionType;
            this.location = location;
        }

        public Builder actor(Actor actor) {
            this.actor = actor;
            return this;
        }

        public Builder blockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        public Builder itemInvolved(ItemStack itemInvolved) {
            this.itemInvolved = itemInvolved;
            return this;
        }

        public ActionContext build() {
            return new ActionContext(this);
        }
    }
}
