package me.cable.dynamicpets.handler;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            // copy dog file
            try (InputStream in = DynamicPets.class.getResourceAsStream("/pet.yml")) {
                Files.copy(in, new File(petsFolder, "dog.yml").toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] children = petsFolder.list();
        if (children == null) return;

        for (String fileName : children) {
            String petId = fileName.substring(0, fileName.length() - 4);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(petsFolder, fileName));
            petConfigs.put(petId, config);
        }
    }

    public @NotNull List<String> getPetIds() {
        return petConfigs.keySet().stream().toList();
    }

    public @NotNull ItemStack getHead(@NotNull String petType) {
        String headTexture = getPetConfig(petType).getString("head-texture");
        return (headTexture == null) ? new ItemStack(Material.PLAYER_HEAD) : Utils.createHead(headTexture);
    }

    public boolean isValidPetType(@NotNull String petType) {
        return petConfigs.containsKey(petType);
    }

    public @NotNull YamlConfiguration getPetConfig(@NotNull String petType) {
        return petConfigs.getOrDefault(petType, new YamlConfiguration());
    }
}
