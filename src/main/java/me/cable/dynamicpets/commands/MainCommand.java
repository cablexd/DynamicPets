package me.cable.dynamicpets.commands;

import me.cable.dynamicpets.util.VersionSpecific;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends AbstractCommand {

    private ArmorStand armorStand;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "Server is running " + dynamicPets.getName() + " version " + dynamicPets.getDescription().getVersion() + ".");
            return true;
        }

        Player player = (Player) commandSender;
        if (args[0].equals("create")) {
            armorStand = VersionSpecific.createArmorStand(player.getWorld());
            armorStand.setInvisible(true);
            armorStand.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD));

            VersionSpecific.updateData(armorStand);
            VersionSpecific.updateEquipment(armorStand);

            commandSender.sendMessage(ChatColor.GREEN + "Created armor stand.");
        } else if (args[0].equals("move")) {
            Location l = player.getLocation();
            VersionSpecific.teleport(armorStand, l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            commandSender.sendMessage(ChatColor.GREEN + "Updated position of armor stand.");
        }

        return true;
    }
}
