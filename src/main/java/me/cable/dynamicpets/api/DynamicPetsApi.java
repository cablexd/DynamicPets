package me.cable.dynamicpets.api;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.handler.PlayerHandler;
import me.cable.dynamicpets.menu.PetsMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DynamicPetsApi {

    public static @NotNull List<Pet> getPets(@NotNull Player player) {
        return DynamicPets.getInstance().getPlayerHandler().getPets(player).stream().map(Pet::new).toList();
    }

    public static @NotNull List<Pet> getEquippedPets(@NotNull Player player) {
        return DynamicPets.getInstance().getPlayerHandler().getEquippedPets(player).stream().map(v -> new Pet(v.getPet())).toList();
    }

    public static int getPetSlots(@NotNull Player player) {
        return PlayerHandler.getPetSlots(player);
    }

    public static @NotNull String getPetName(@NotNull String petType) {
        return DynamicPets.getInstance().getPetsConfigHandler().getPetConfig(petType).getString("name", petType);
    }

    public static @NotNull List<String> getPetDescription(@NotNull String petType) {
        return DynamicPets.getInstance().getPetsConfigHandler().getPetConfig(petType).getStringList("description");
    }

    public static boolean isPetSmall(@NotNull String petType) {
        return DynamicPets.getInstance().getPetsConfigHandler().getPetConfig(petType).getBoolean("small");
    }

    public static void openPetsMenu(@NotNull Player player) {
        new PetsMenu(player).open();
    }
}
