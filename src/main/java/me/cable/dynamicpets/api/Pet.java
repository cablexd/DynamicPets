package me.cable.dynamicpets.api;

import me.cable.dynamicpets.DynamicPets;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/*
    Serves as a wrapper for the internal pet and some PlayerHandler methods.
 */
public class Pet {

    private final me.cable.dynamicpets.instance.Pet pet;

    public Pet(@NotNull me.cable.dynamicpets.instance.Pet pet) {
        this.pet = pet;
    }

    public @NotNull ItemStack getPlayerHead() {
        return DynamicPets.getInstance().getPetsConfigHandler().getHead(getType());
    }

    public @NotNull String getType() {
        return pet.getType();
    }

    public void giveToPlayer(@NotNull Player player) {
        if (!playerHas(player)) {
            DynamicPets.getInstance().getPlayerHandler().givePet(player, pet);
        }
    }

    public void removeFromPlayer(@NotNull Player player) {
        DynamicPets.getInstance().getPlayerHandler().removePet(player, pet);
    }

    public boolean playerHas(@NotNull Player player) {
        return DynamicPets.getInstance().getPlayerHandler().getPets(player).contains(pet);
    }

    public boolean isEquipped(@NotNull Player player) {
        return DynamicPets.getInstance().getPlayerHandler().isPetEquipped(player, pet);
    }

    public void setEquipped(@NotNull Player player, boolean equipped) {
        if (isEquipped(player) != equipped) {
            if (equipped) {
                DynamicPets.getInstance().getPlayerHandler().equipPet(player, pet, true);
            } else {
                DynamicPets.getInstance().getPlayerHandler().unequipPet(player, pet, true);
            }
        }
    }
}
