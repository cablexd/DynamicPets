package me.cable.dynamicpets.instance;

import org.jetbrains.annotations.NotNull;

/*
    Represents a pet loaded from configuration.
 */
public class Pet {

    private final String type;

    public Pet(@NotNull String type) {
        this.type = type;
    }

    public @NotNull String getType() {
        return type;
    }
}
