package com.nationwar.nation.common;

import java.util.UUID;

/**
 * Represents a nation.
 * This class holds the core properties of a nation.
 */
public final class Nation {
    private final UUID id;
    private final String name;
    private int color;

    public Nation(UUID id, String name, int initialColor) {
        this.id = id;
        this.name = name;
        this.color = initialColor;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
