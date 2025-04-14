package me.cable.dynamicpets;

import me.cable.dynamicpets.commands.MainCommand;
import me.cable.dynamicpets.commands.PetsCommand;
import me.cable.dynamicpets.handler.ConfigHandler;
import me.cable.dynamicpets.handler.MovementHandler;
import me.cable.dynamicpets.handler.PetsConfigHandler;
import me.cable.dynamicpets.handler.PlayerHandler;
import me.cable.dynamicpets.listeners.InventoryClick;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class DynamicPets extends JavaPlugin {

    private ConfigHandler configHandler;
    private PetsConfigHandler petsConfigHandler;
    private PlayerHandler playerHandler;

    public static @NotNull DynamicPets getInstance() {
        return JavaPlugin.getPlugin(DynamicPets.class);
    }

    @Override
    public void onEnable() {
        initializeHandlers();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        playerHandler.savePlayerData();
    }

    private void initializeHandlers() {
        configHandler = new ConfigHandler(this);
        petsConfigHandler = new PetsConfigHandler(this);
        playerHandler = new PlayerHandler(this);
        new MovementHandler(this).start();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryClick(), this);
    }

    private void registerCommands() {
        new MainCommand().register("dynamicpets");
        new PetsCommand().register("pets");
    }

    public @NotNull ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public @NotNull PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public @NotNull PetsConfigHandler getPetsConfigHandler() {
        return petsConfigHandler;
    }
}
