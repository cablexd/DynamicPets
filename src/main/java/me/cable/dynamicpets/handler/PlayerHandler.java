package me.cable.dynamicpets.handler;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.instance.EquippedPet;
import me.cable.dynamicpets.instance.Pet;
import me.cable.dynamicpets.instance.movement.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHandler implements Listener {

    private final DynamicPets dynamicPets;
    private final PetsConfigHandler petsConfigHandler;

    private final Map<Player, PlayerData> playerData = new HashMap<>();

    public PlayerHandler(@NotNull DynamicPets dynamicPets) {
        this.dynamicPets = dynamicPets;
        petsConfigHandler = dynamicPets.getPetsConfigHandler();

        Bukkit.getServer().getPluginManager().registerEvents(this, dynamicPets);
    }

    private @NotNull PlayerData getPlayerData(@NotNull Player player) {
        return playerData.get(player);
    }

    private @NotNull File getPlayerFile(@NotNull Player player) {
        return new File(new File(dynamicPets.getDataFolder(), "PlayerData"), player.getUniqueId() + ".yml");
    }

    /*
        Remove all visible pets and create new ones.
     */
    public void reloadPets() {
        for (Map.Entry<Player, PlayerData> entry : playerData.entrySet()) {
            Player player = entry.getKey();
            PlayerData playerData = entry.getValue();
            List<Pet> storedEquippedPets = new ArrayList<>();

            for (EquippedPet equippedPet : playerData.equippedPets) {
                storedEquippedPets.add(equippedPet.getPet());
                equippedPet.getEntityDisplay().setWorld(null); // hide
            }

            playerData.equippedPets.clear();
            storedEquippedPets.forEach(v -> equipPet(player, v));
        }
    }

    public void givePet(@NotNull Player player, @NotNull Pet pet) {
        getPlayerData(player).pets.add(pet);
    }

    public void removePet(@NotNull Player player, @NotNull Pet pet) {
        getPlayerData(player).pets.remove(pet);
    }

    public @NotNull EquippedPet equipPet(@NotNull Player player, @NotNull Pet pet) {
        EquippedPet equippedPet = new EquippedPet(pet, player);

        for (Movement movement : List.of(
                new FollowMovement(equippedPet),
                new JumpMovement(equippedPet),
                new OrbitMovement(equippedPet),
                new TeleportMovement(equippedPet),
                new WalkMovement(equippedPet)
        )) {
            if (equippedPet.getSettings().getBoolean("movements." + movement.getId() + ".enabled")) {
                equippedPet.addMovement(movement);
            }
        }

        getPlayerData(player).equippedPets.add(equippedPet);
        // TODO: update inventory
        return equippedPet;
    }

    public void unequipPet(@NotNull Player player, @NotNull EquippedPet equippedPet) {
        equippedPet.getEntityDisplay().setWorld(null); // hide pet
        getPlayerData(player).equippedPets.remove(equippedPet);
        // TODO: update inventory
    }

    public @NotNull List<Pet> getPets(@NotNull Player player) {
        return List.copyOf(getPlayerData(player).pets);
    }

    public @NotNull List<EquippedPet> getEquippedPets(@NotNull Player player) {
        return List.copyOf(getPlayerData(player).equippedPets);
    }

    private @NotNull List<EquippedPet> getEquippedPets(@NotNull World world) {
        List<EquippedPet> list = new ArrayList<>();

        for (Map.Entry<Player, PlayerData> entry : playerData.entrySet()) {
            for (EquippedPet equippedPet : entry.getValue().equippedPets) {
                if (world.equals(equippedPet.getEntityDisplay().getWorld())) {
                    list.add(equippedPet);
                }
            }
        }

        return list;
    }

    private void showPetsInWorld(@NotNull Player player) {
        // show the pets in the player's new world
        World world = player.getWorld();

        for (EquippedPet equippedPet : getEquippedPets(world)) {
            equippedPet.getEntityDisplay().show(player);
        }
    }

    private void loadPlayerData(@NotNull Player player) {
        PlayerData pd = new PlayerData();
        playerData.put(player, pd);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(player));
        ConfigurationSection petsSection = config.getConfigurationSection("pets");
        List<Integer> equippedPetIds = config.getIntegerList("equipped-pets");

        if (petsSection != null) {
            for (String key : petsSection.getKeys(false)) {
                String petType = petsSection.getString(key + ".type");

                if (petType != null && petsConfigHandler.isValidPetType(petType)) {
                    Pet pet = new Pet(petType);
                    pd.pets.add(pet);

                    // check if pet was equipped
                    if (equippedPetIds.contains(Integer.parseInt(key))) {
                        equipPet(player, pet);
                    }
                }
            }
        }
    }

    private void savePlayerData(@NotNull Player player) {
        YamlConfiguration saving = new YamlConfiguration();
        PlayerData pd = getPlayerData(player);
        List<Pet> equipped = pd.equippedPets.stream().map(EquippedPet::getPet).toList();
        List<Integer> equippedPetKeys = new ArrayList<>();

        ConfigurationSection petsSection = saving.createSection("pets");
        int nextKey = 0;

        // save pets
        for (Pet pet : pd.pets) {
            ConfigurationSection petSection = petsSection.createSection(Integer.toString(nextKey));
            petSection.set("type", pet.getType());

            if (equipped.contains(pet)) {
                equippedPetKeys.add(nextKey);
            }

            nextKey++;
        }

        // save equipped pets
        saving.set("equipped-pets", equippedPetKeys);

        try {
            saving.save(getPlayerFile(player));
        } catch (IOException e) {
            dynamicPets.getLogger().severe("Unable to save player data: " + player.getUniqueId());
            e.printStackTrace();
        }
    }

    @EventHandler
    private void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerData(player);
        showPetsInWorld(player);
    }

    @EventHandler
    private void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // hide pets in game
        for (EquippedPet equippedPet : getPlayerData(player).equippedPets) {
            equippedPet.getEntityDisplay().setWorld(null);
        }

        // save& remove player data
        savePlayerData(player);
        playerData.remove(event.getPlayer());
    }

    @EventHandler
    private void onPlayerChangedWorld(@NotNull PlayerChangedWorldEvent event) {
        showPetsInWorld(event.getPlayer());
    }

    private static class PlayerData {
        final List<Pet> pets = new ArrayList<>();
        final List<EquippedPet> equippedPets = new ArrayList<>();
    }
}
