package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class FollowMovement extends Movement {

    private int taskId;

    public FollowMovement(@NotNull EquippedPet equippedPet) {
        super("follow", equippedPet);
    }

    @Override
    public boolean selectRandomly() {
        return false;
    }

    @Override
    public boolean override() {
        // check if player is far enough to trigger following
        double distance = getSettings().getDouble("trigger-distance");
        return player.getLocation().toVector().distanceSquared(entityDisplay.getPosition()) > distance * distance;
    }

    private double extraSpeed() {
        // make pet faster when further away
        double distance = player.getLocation().toVector().distance(entityDisplay.getPosition());
        return Math.min(Math.pow(1.013, Math.max(distance - 10, 0)) - 1, 2);
    }

    @Override
    public void start() {
        double angle = Math.random() * Math.PI * 2;
        double radius = random.nextDouble(getSettings().getDouble("target-radius.min"), getSettings().getDouble("target-radius.max"));
        double x = Math.sin(angle) * radius;
        double z = Math.cos(angle) * radius;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DynamicPets.getInstance(), () -> {
            Vector from = entityDisplay.getPosition();
            Vector to = getGroundLocation(player.getLocation().add(x, 0, z)).toVector();
            Vector dir = to.clone().subtract(from).normalize();
            float yaw = (float) Math.toDegrees(Math.atan2(dir.getZ(), dir.getX())) - 90; // face moving direction

            from.add(dir.multiply(getSettings().getDouble("speed") + extraSpeed()));

            if (from.distanceSquared(to) > GOAL_RANGE * GOAL_RANGE) {
                entityDisplay.setPosition(from.getX(), from.getY(), from.getZ(), yaw);
            } else {
                entityDisplay.setPosition(to.getX(), to.getY(), to.getZ(), yaw);
                end();
            }
        }, 0, 1);
    }

    @Override
    public void onEnd() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
