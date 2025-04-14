package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.instance.EntityDisplay;
import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public abstract class Movement {

    public static final double GOAL_RANGE = 0.5;

    private final String id;
    protected final EquippedPet equippedPet;
    protected final EntityDisplay entityDisplay;
    protected final Player player; // pet owner

    protected final Random random = new Random();

    public Movement(@NotNull String id, @NotNull EquippedPet equippedPet) {
        this.id = id;
        this.equippedPet = equippedPet;
        this.entityDisplay = equippedPet.getEntityDisplay();
        this.player = equippedPet.getOwner();
    }

    /*
        If this returns false, this movement will not be selected randomly.
        It will only be used if its override function returns true.
     */
    public boolean selectRandomly() {
        return true;
    }

    /*
        Return true to stop the current movement and start this one.
     */
    public boolean override() {
        return false;
    }

    public abstract void start();

    public final void end() {
        onEnd();
        equippedPet.setCurrentMovement(null);
    }

    public void onEnd() {
        // empty
    }

    protected final @NotNull ConfigurationSection getSettings() {
        ConfigurationSection cs = equippedPet.getSettings().getConfigurationSection("movements." + id);
        return (cs == null) ? new YamlConfiguration() : cs;
    }

    protected final @NotNull Location getGroundLocation(@NotNull Location loc) {
        loc = loc.clone(); // prevent modifying passed in location
        Block groundBlock = loc.getBlock();

        if (groundBlock.getType().isAir()) {
            for (int i = 0; i < 10 && groundBlock.getType().isAir(); i++) {
                groundBlock = groundBlock.getRelative(BlockFace.DOWN);
            }

            if (!groundBlock.getType().isAir()) {
                loc.setY(groundBlock.getY() + 1);
                return loc;
            }
        } else {
            for (int i = 0; i < 10 && groundBlock.getType() != Material.AIR; i++) {
                groundBlock = groundBlock.getRelative(BlockFace.UP);
            }

            if (groundBlock.getType().isAir()) {
                loc.setY(groundBlock.getY());
                return loc;
            }
        }

        return loc;
    }

    public final @NotNull String getId() {
        return id;
    }
}
