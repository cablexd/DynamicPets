package me.cable.dynamicpets.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NmsUtils {

    private static @NotNull net.minecraft.world.entity.EquipmentSlot map(@NotNull EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case HAND -> net.minecraft.world.entity.EquipmentSlot.MAINHAND;
            case OFF_HAND -> net.minecraft.world.entity.EquipmentSlot.OFFHAND;
            case HEAD -> net.minecraft.world.entity.EquipmentSlot.HEAD;
            case CHEST -> net.minecraft.world.entity.EquipmentSlot.CHEST;
            case LEGS -> net.minecraft.world.entity.EquipmentSlot.LEGS;
            case FEET -> net.minecraft.world.entity.EquipmentSlot.FEET;
            case BODY -> net.minecraft.world.entity.EquipmentSlot.BODY;
        };
    }

    private static void sendPacket(@NotNull Packet<?> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }

    private static void sendPacket(@NotNull Packet<?> packet, @NotNull Player player) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    public static @NotNull ArmorStand createArmorStand() {
        World world = Bukkit.getWorlds().getFirst();
        net.minecraft.world.entity.decoration.ArmorStand a = new net.minecraft.world.entity.decoration.ArmorStand(((CraftWorld) world).getHandle(), 0, 0, 0);
        return (ArmorStand) a.getBukkitEntity();
    }

    // create entity for player
    public static void show(@NotNull Player player, @NotNull Entity entity) {
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(((CraftEntity) entity).getHandle(), 0, new BlockPos(0, 0, 0));
        sendPacket(packet, player);
    }

    // remove entity for player
    public static void hide(@NotNull Player player, @NotNull Entity entity) {
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(entity.getEntityId());
        sendPacket(packet, player);
    }

    public static void updateData(@NotNull Player player, @NotNull Entity entity) {
        net.minecraft.world.entity.Entity e = ((CraftEntity) entity).getHandle();
        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(e.getId(), e.getEntityData().getNonDefaultValues());
        sendPacket(packet, player);
    }

    public static void updateEquipment(@NotNull Player player, @NotNull ArmorStand entity) {
        net.minecraft.world.entity.Entity e = ((CraftEntity) entity).getHandle();
        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();

        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack item = entity.getEquipment().getItem(equipmentSlot);
            list.add(new Pair<>(map(equipmentSlot), CraftItemStack.asNMSCopy(item)));
        }

        ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(e.getId(), list);
        sendPacket(packet, player);
    }

    public static void teleport(@NotNull Player player, @NotNull ArmorStand armorStand, double x, double y, double z, float yaw, float pitch) {
        net.minecraft.world.entity.decoration.ArmorStand a = ((CraftArmorStand) armorStand).getHandle();
        PositionMoveRotation pmr = new PositionMoveRotation(new Vec3(x, y, z), new Vec3(0, 0, 0), yaw, pitch);
        ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(a.getId(), pmr, Collections.emptySet(), a.onGround);
        sendPacket(packet, player);
    }
}
