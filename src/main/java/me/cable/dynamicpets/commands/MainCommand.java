package me.cable.dynamicpets.commands;

import me.cable.dynamicpets.instance.Pet;
import me.cable.dynamicpets.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(Component.text(
                    "Server is running " + dynamicPets.getName() + " version " + dynamicPets.getDescription().getVersion() + ".", NamedTextColor.GRAY));
            return true;
        }

        switch (args[0]) {
            case "give" -> {
                if (args.length < 3) {
                    commandSender.sendMessage(Component.text("Usage: /" + s + " give <player> <pet>", NamedTextColor.RED));
                    return true;
                }

                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);

                if (player == null) {
                    commandSender.sendMessage(Component.text("The target player could not be found!", NamedTextColor.RED));
                    return true;
                }

                String petType = args[2];
                Pet pet = new Pet(petType);
                playerHandler.givePet(player, pet);
                commandSender.sendMessage(Utils.miniMessage(String.format("<green>Gave <gold>%s</gold> pet of type <gold>%s</gold>.</green>", playerName, petType)));
            }
            case "reload" -> {
                configHandler.load();
                petsConfigHandler.load();
                playerHandler.reloadPets();
                commandSender.sendMessage(Component.text("Configuration reloaded.", NamedTextColor.GREEN));
            }
            default -> commandSender.sendMessage(Component.text("Unknown command!", NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String string : List.of("give", "reload")) {
                if (string.startsWith(args[0])) {
                    result.add(string);
                }
            }
        } else if (args[0].equals("give")) {
            if (args.length == 2) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().startsWith(args[1])) {
                        result.add(player.getName());
                    }
                }
            } else if (args.length == 3) {
                for (String petId : petsConfigHandler.getPetIds()) {
                    if (petId.startsWith(args[2])) {
                        result.add(petId);
                    }
                }
            }
        }

        return result;
    }
}
