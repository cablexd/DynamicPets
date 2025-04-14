package me.cable.dynamicpets.commands;

import me.cable.dynamicpets.menu.PetsMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PetsCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Component.text("Only players may use this command!").color(NamedTextColor.RED));
            return true;
        }

        new PetsMenu(player).open();
        return true;
    }
}
