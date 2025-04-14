package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.DynamicPets;
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

    public static final double BODY_HEIGHT_BIG = 1.43;
    public static final double BODY_HEIGHT_SMALL = 0.72;
    public static final double GOAL_RANGE = 0.3;

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

    public static double getBodyHeight(boolean small) {
        return small ? BODY_HEIGHT_SMALL : BODY_HEIGHT_BIG;
    }

    public double getBodyHeight() {
        return getBodyHeight(entityDisplay.isSmall());
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

    protected final void end() {
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
        double bodyHeight = getBodyHeight();

        if (groundBlock.getType().isAir()) {
            for (int i = 0; i < 10 && groundBlock.getType().isAir(); i++) {
                groundBlock = groundBlock.getRelative(BlockFace.DOWN);
            }

            if (!groundBlock.getType().isAir()) {
                loc.setY(groundBlock.getY() + 1 - bodyHeight);
                return loc;
            }
        } else {
            for (int i = 0; i < 10 && groundBlock.getType() != Material.AIR; i++) {
                groundBlock = groundBlock.getRelative(BlockFace.UP);
            }

            if (groundBlock.getType().isAir()) {
                loc.setY(groundBlock.getY() - bodyHeight);
                return loc;
            }
        }

        loc.setY(loc.getY() - bodyHeight);
        return loc;
    }

    public final @NotNull String getId() {
        return id;
    }
}
