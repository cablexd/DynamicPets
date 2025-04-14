package me.cable.dynamicpets.commands;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.handler.PetsConfigHandler;
import me.cable.dynamicpets.handler.PlayerHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand implements TabExecutor {

    protected final DynamicPets dynamicPets;
    protected final PlayerHandler playerHandler;
    protected final PetsConfigHandler petsConfigHandler;

    public AbstractCommand() {
        dynamicPets = JavaPlugin.getPlugin(DynamicPets.class);
        playerHandler = dynamicPets.getPlayerHandler();
        petsConfigHandler = dynamicPets.getPetsConfigHandler();
    }

    public void register(@NotNull String label) {
        PluginCommand pluginCommand = dynamicPets.getCommand(label);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
