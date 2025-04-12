package me.cable.dynamicpets;

import me.cable.dynamicpets.commands.MainCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DynamicPets extends JavaPlugin {

    @Override
    public void onEnable() {
        initializeHandlers();
        registerCommands();
    }

    private void initializeHandlers() {

    }

    private void registerCommands() {
        new MainCommand().register("dynamicpets");
    }
}
