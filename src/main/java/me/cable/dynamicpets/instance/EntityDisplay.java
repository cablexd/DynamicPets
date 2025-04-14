package me.cable.dynamicpets.instance;

import me.cable.dynamicpets.util.NmsUtils;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/*
    A client-side armor stand handled with packets.
 */
public class EntityDisplay {

    private static final double BODY_HEIGHT_BIG = 1.43;
    private static final double BODY_HEIGHT_SMALL = 0.72;

    private final ArmorStand armorStand;
    private @Nullable World world;
    private double x, y, z;
    private float yaw;

    public EntityDisplay() {
        armorStand = NmsUtils.createArmorStand();
    }

    private void forPlayers(@NotNull Consumer<Player> consumer) {
        if (world != null) {
            world.getPlayers().forEach(consumer);
        }
    }

    private void updatePosition(@NotNull Player player) {
        NmsUtils.teleport(player, armorStand, x, y - (armorStand.isSmall() ? BODY_HEIGHT_SMALL : BODY_HEIGHT_BIG), z, yaw, 0);
    }

    /*
       Update properties like arms, glowing, baseplate, etc.
    */
    private void updateData(@NotNull Player player) {
        NmsUtils.updateData(player, armorStand);
    }

    public void updateData(@NotNull Consumer<ArmorStand> consumer) {
        consumer.accept(armorStand);
        forPlayers(this::updateData);
    }

    private void updateEquipment(@NotNull Player player) {
        NmsUtils.updateEquipment(player, armorStand);
    }

    public void updateEquipment(@NotNull Consumer<EntityEquipment> consumer) {
        consumer.accept(armorStand.getEquipment());
        forPlayers(this::updateEquipment);
    }

    public void show(@NotNull Player player) {
        NmsUtils.show(player, armorStand);
        updatePosition(player);
        updateData(player);
        updateEquipment(player);
    }

    public @Nullable World getWorld() {
        return world;
    }

    public void setWorld(@Nullable World world) {
        if (!Objects.equals(this.world, world)) {
            forPlayers(player -> NmsUtils.hide(player, armorStand)); // remove from current world
            this.world = world;
            forPlayers(this::show); // add to new world
        }
    }

    public @NotNull Vector getPosition() {
        return new Vector(x, y, z);
    }

    public void setPosition(double x, double y, double z, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        forPlayers(this::updatePosition);
    }

    public void setPosition(double x, double y, double z) {
        setPosition(x, y, z, yaw);
    }
}
