package me.cable.dynamicpets;

import me.cable.dynamicpets.util.VersionSpecific;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Pet {

    private final ArmorStand armorStand;

    public Pet(@NotNull Player owner) {
        armorStand = VersionSpecific.createArmorStand(owner.getWorld());
    }
}
