package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class TeleportMovement extends Movement {

    public TeleportMovement(@NotNull EquippedPet equippedPet) {
        super("teleport", equippedPet);
    }

    @Override
    public boolean selectRandomly() {
        return false;
    }

    @Override
    public boolean override() {
        double triggerDistance = getSettings().getDouble("trigger-distance");
        // teleport if far away in same world
        return player.getWorld().equals(entityDisplay.getWorld())
                && entityDisplay.getPosition().distanceSquared(player.getLocation().toVector()) > triggerDistance * triggerDistance;
    }

    @Override
    public void start() {
        Location playerLoc = player.getLocation();
        entityDisplay.setPosition(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getYaw());
        end();
    }
}
