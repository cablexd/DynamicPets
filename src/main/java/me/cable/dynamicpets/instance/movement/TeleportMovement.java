package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
        Vector pos = entityDisplay.getPosition();
        Location playerLoc = player.getLocation();
        return !Objects.equals(playerLoc.getWorld(), entityDisplay.getWorld())
                || pos.distanceSquared(playerLoc.toVector()) > triggerDistance * triggerDistance;
    }

    @Override
    public void start() {
        Location playerLoc = player.getLocation();
        entityDisplay.setWorld(player.getWorld());
        entityDisplay.setPosition(playerLoc.getX(), playerLoc.getY() - getBodyHeight(), playerLoc.getZ(), playerLoc.getYaw());
        end();
    }
}
