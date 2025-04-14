package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class WalkMovement extends Movement {

    private int taskId;

    public WalkMovement(@NotNull EquippedPet equippedPet) {
        super("walk", equippedPet);
    }

    private @NotNull Vector selectLoc() {
        double angle = random.nextDouble() * Math.PI * 2;
        double radius = random.nextDouble(getSettings().getDouble("target-radius.min"), getSettings().getDouble("target-radius.max"));
        double x = Math.sin(angle) * radius;
        double z = Math.cos(angle) * radius;
        return getGroundLocation(player.getLocation().add(x, 0, z)).toVector();
    }

    @Override
    public void start() {
        Vector targetPos = selectLoc();

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DynamicPets.getInstance(), () -> {
            Vector from = entityDisplay.getPosition();
            Vector dir = targetPos.clone().subtract(from).normalize();
            float yaw = (float) Math.toDegrees(Math.atan2(dir.getZ(), dir.getX())) - 90;

            from.add(dir.multiply(getSettings().getDouble("speed")));

            if (from.distanceSquared(targetPos) > GOAL_RANGE * GOAL_RANGE) {
                entityDisplay.setPosition(from.getX(), from.getY(), from.getZ(), yaw);
            } else {
                entityDisplay.setPosition(targetPos.getX(), targetPos.getY(), targetPos.getZ(), yaw);
                end();
            }
        }, 0, 1);
    }

    @Override
    public void onEnd() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
