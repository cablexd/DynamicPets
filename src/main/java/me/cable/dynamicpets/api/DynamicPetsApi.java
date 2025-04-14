package me.cable.dynamicpets.api;

import me.cable.dynamicpets.DynamicPets;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DynamicPetsApi {

    public @NotNull List<Pet> getPets(@NotNull Player player) {
        return DynamicPets.getInstance().getPlayerHandler().getPets(player).stream().map(Pet::new).toList();
    }

    public @NotNull List<Pet> getEquippedPets(@NotNull Player player) {
        return DynamicPets.getInstance().getPlayerHandler().getEquippedPets(player).stream().map(v -> new Pet(v.getPet())).toList();
    }
}
