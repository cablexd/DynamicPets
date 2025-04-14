package me.cable.dynamicpets.api.event;

import me.cable.dynamicpets.api.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PetEquipEvent extends PetEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PetEquipEvent(@NotNull Player player, @NotNull Pet pet) {
        super(player, pet);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
