package me.cable.dynamicpets.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(ChatColor.GREEN + "Server is running " + dynamicPets.getName() + " version " + dynamicPets.getDescription().getVersion() + ".");
        return true;
    }
}
