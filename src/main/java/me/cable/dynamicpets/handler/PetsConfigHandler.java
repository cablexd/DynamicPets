package me.cable.dynamicpets.handler;

import me.cable.dynamicpets.DynamicPets;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PetsConfigHandler {

    private final DynamicPets dynamicPets;

    private final Map<String, YamlConfiguration> petConfigs = new HashMap<>();

    public PetsConfigHandler(@NotNull DynamicPets dynamicPets) {
        this.dynamicPets = dynamicPets;
        load();
    }

    public void load() {
        petConfigs.clear();
        File petsFolder = new File(dynamicPets.getDataFolder(), "Pets");

        if (!petsFolder.exists()) {
            petsFolder.mkdirs();
            return;
        }
        if (!petsFolder.isDirectory()) {
            return;
        }

        String[] children = petsFolder.list();
        if (children == null) return;

        for (String fileName : children) {
            String petId = fileName.substring(0, fileName.length() - 4);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(petsFolder, fileName));
            petConfigs.put(petId, config);
        }
    }

    public boolean isValidPetType(@NotNull String petType) {
        return petConfigs.containsKey(petType);
    }

    public @NotNull YamlConfiguration getPetConfig(@NotNull String petType) {
        return petConfigs.getOrDefault(petType, new YamlConfiguration());
    }
}
