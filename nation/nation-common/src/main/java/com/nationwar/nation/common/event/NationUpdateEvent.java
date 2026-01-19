package com.nationwar.nation.common.event;

import com.nationwar.nation.common.Nation;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired when a nation's properties (e.g., color) are updated.
 */
public class NationUpdateEvent extends Event {
    private final Nation nation;

    public NationUpdateEvent(Nation nation) {
        this.nation = nation;
    }

    public Nation getNation() {
        return nation;
    }
}
