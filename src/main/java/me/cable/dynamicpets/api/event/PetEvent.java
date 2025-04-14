package me.cable.dynamicpets.api.event;

import me.cable.dynamicpets.api.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class PetEvent extends Event {

    private final Player player;
    private final Pet pet;

    public PetEvent(@NotNull Player player, @NotNull Pet pet) {
        this.player = player;
        this.pet = pet;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Pet getPet() {
        return pet;
    }
}
