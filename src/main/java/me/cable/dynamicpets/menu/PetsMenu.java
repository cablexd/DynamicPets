package me.cable.dynamicpets.menu;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.handler.ConfigHandler;
import me.cable.dynamicpets.handler.PetsConfigHandler;
import me.cable.dynamicpets.handler.PlayerHandler;
import me.cable.dynamicpets.instance.EquippedPet;
import me.cable.dynamicpets.instance.Pet;
import me.cable.dynamicpets.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PetsMenu implements InventoryHolder {

    private final ConfigHandler configHandler;
    private final PetsConfigHandler petsConfigHandler;
    private final PlayerHandler playerHandler;

    private final Player player;
    private final List<Pet> pets;

    private final NamespacedKey itemKey;

    public PetsMenu(@NotNull Player player) {
        DynamicPets dynamicPets = DynamicPets.getInstance();
        configHandler = dynamicPets.getConfigHandler();
        petsConfigHandler = dynamicPets.getPetsConfigHandler();
        playerHandler = dynamicPets.getPlayerHandler();

        this.player = player;
        pets = playerHandler.getPets(player);

        itemKey = new NamespacedKey(dynamicPets, "pet-index");
    }

    public static void update(@NotNull Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof PetsMenu petsMenu) {
            petsMenu.update();
        }
    }

    public void open() {
        player.openInventory(getInventory());
    }

    private void update() {
        open();
    }

    private @NotNull ConfigurationSection getItemSection(boolean equipped) {
        ConfigurationSection cs = configHandler.getConfig().getConfigurationSection("pets-menu.pet-item." + (equipped ? "equipped" : "unequipped"));
        return Objects.requireNonNullElse(cs, new YamlConfiguration());
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 9 * 3, Utils.miniMessage("Pets"));

        for (int i = 0; i < pets.size() && i < inv.getSize(); i++) {
            Pet pet = pets.get(i);
            YamlConfiguration petConfig = petsConfigHandler.getPetConfig(pet.getType());
            boolean equipped = playerHandler.isPetEquipped(player, pet);
            ConfigurationSection itemSection = getItemSection(equipped);

            ItemStack item = petsConfigHandler.getHead(pet.getType());
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                // set name
                String itemName = itemSection.getString("name");
                String petName = petConfig.getString("name", "");
                Component nameComponent;

                if (itemName == null) {
                    nameComponent = Component.text(petName);
                } else {
                    nameComponent = Utils.miniMessage(itemName.replace("{name}", petName));
                }

                meta.displayName(nameComponent);

                // set lore
                List<String> lore = itemSection.getStringList("lore");
                Utils.insertListToList(lore, "{description}", petConfig.getStringList("description"));
                meta.lore(Utils.miniMessage(lore));

                meta.getPersistentDataContainer().set(itemKey, PersistentDataType.INTEGER, i);
                item.setItemMeta(meta);
            }

            inv.setItem(i, item);
        }

        return inv;
    }

    public void onClick(@NotNull InventoryClickEvent e) {
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        Integer petIndex = meta.getPersistentDataContainer().get(itemKey, PersistentDataType.INTEGER);
        if (petIndex == null) return;

        Pet pet = pets.get(petIndex);
        List<EquippedPet> equippedPets = playerHandler.getEquippedPets(player);

        if (playerHandler.isPetEquipped(player, pet)) {
            // unequip
            playerHandler.unequipPet(player, pet, true);
            update();
        } else {
            // try equip
            int max = PlayerHandler.getPetSlots(player);

            if (equippedPets.size() < max) {
                playerHandler.equipPet(player, pet, true);
                update();
            } else {
                player.sendMessage(Component.text("You cannot equip any more pets!").color(NamedTextColor.RED));
            }
        }
    }
}
