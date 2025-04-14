package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class OrbitMovement extends Movement {

    private BukkitTask task;

    public OrbitMovement(@NotNull EquippedPet equippedPet) {
        super("orbit", equippedPet);
    }

    private @NotNull Location calcPos(double angleDeg, double radius) {
        double rad = Math.toRadians(angleDeg);
        double x = Math.sin(rad) * radius;
        double z = Math.cos(rad) * radius;
        Location loc = player.getLocation().add(x, 1 - getBodyHeight(), z);

        float yaw = (float) angleDeg;
        if (yaw < 0) yaw = 360 + yaw;
        loc.setYaw(180 - yaw);

        return loc;
    }

    @Override
    public void start() {
        int duration = getSettings().getInt("duration");
        int period = getSettings().getInt("period");
        double radius = getSettings().getInt("radius");
        double angleInterval = 360.0 / period;

        task = new BukkitRunnable() {

            int time;
            double angle;

            @Override
            public void run() {
                angle = (angle + angleInterval) % 360;
                Location loc = calcPos(angle, radius);
                entityDisplay.setPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw());

                if (++time >= duration) {
                    end();
                }
            }
        }.runTaskTimer(DynamicPets.getInstance(), 0, 1);
    }

    @Override
    public void onEnd() {
        task.cancel();
        task = null;
    }
}
