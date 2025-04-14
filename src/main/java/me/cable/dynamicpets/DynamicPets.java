package me.cable.dynamicpets;

import me.cable.dynamicpets.commands.MainCommand;
import me.cable.dynamicpets.handler.MovementHandler;
import me.cable.dynamicpets.handler.PetsConfigHandler;
import me.cable.dynamicpets.handler.PlayerHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class DynamicPets extends JavaPlugin {

    private PetsConfigHandler petsConfigHandler;
    private PlayerHandler playerHandler;

    public static @NotNull DynamicPets getInstance() {
        return JavaPlugin.getPlugin(DynamicPets.class);
    }

    @Override
    public void onEnable() {
        initializeHandlers();
        registerCommands();
    }

    private void initializeHandlers() {
        petsConfigHandler = new PetsConfigHandler(this);
        playerHandler = new PlayerHandler(this);
        new MovementHandler(this).start();
    }

    private void registerCommands() {
        new MainCommand().register("dynamicpets");
    }

    public @NotNull PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public @NotNull PetsConfigHandler getPetsConfigHandler() {
        return petsConfigHandler;
    }
}
