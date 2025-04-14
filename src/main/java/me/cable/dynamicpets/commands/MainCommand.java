package me.cable.dynamicpets.commands;

import me.cable.dynamicpets.instance.Pet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "Server is running " + dynamicPets.getName() + " version " + dynamicPets.getDescription().getVersion() + ".");
            return true;
        }

        switch (args[0]) {
            case "pet" -> {
                if (!(commandSender instanceof Player player)) {
                    commandSender.sendMessage(ChatColor.RED + "Only players may use this command!");
                    return true;
                }

                Pet pet = new Pet("dog");
                playerHandler.givePet(player, pet);
                playerHandler.equipPet(player, pet);
                commandSender.sendMessage(ChatColor.GREEN + "Equipped pet dog.");
            }
            case "reload" -> {
                petsConfigHandler.load();
                playerHandler.reloadPets();
                commandSender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
            }
            default -> commandSender.sendMessage(ChatColor.RED + "Unknown command!");
        }

        return true;
    }
}
