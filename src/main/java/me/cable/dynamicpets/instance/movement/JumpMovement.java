package me.cable.dynamicpets.instance.movement;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.instance.EquippedPet;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class JumpMovement extends Movement {

    private BukkitTask task;

    public JumpMovement(@NotNull EquippedPet equippedPet) {
        super("jump", equippedPet);
    }

    private double calcHeight(int time, int duration, double maxHeight) {
        double a = -4 * maxHeight / (duration * duration);
        double p = duration / 2.0;
        return a * NumberConversions.square(time - p) + maxHeight;
    }

    @Override
    public void start() {
        Vector startingPos = entityDisplay.getPosition();
        double height = getSettings().getDouble("height");
        int duration = getSettings().getInt("duration");

        task = new BukkitRunnable() {

            int time;

            @Override
            public void run() {
                time++;
                double currentHeight = calcHeight(time, duration, height);

                Vector newPos = startingPos.clone().add(new Vector(0, currentHeight, 0));
                entityDisplay.setPosition(newPos.getX(), newPos.getY(), newPos.getZ());

                if (time >= duration) {
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
