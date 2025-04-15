package me.cable.dynamicpets.handler;

import me.cable.dynamicpets.DynamicPets;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigHandler {

    private final File configFile;
    private YamlConfiguration config;

    public ConfigHandler(@NotNull DynamicPets dynamicPets) {
        configFile = new File(dynamicPets.getDataFolder(), "config.yml");
        load();
    }

    public void load() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();

            try (InputStream in = DynamicPets.class.getResourceAsStream("/config.yml")) {
                Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public @NotNull YamlConfiguration getConfig() {
        return config;
    }
}
