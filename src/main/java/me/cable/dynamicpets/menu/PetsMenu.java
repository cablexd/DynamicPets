package me.cable.dynamicpets.menu;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.handler.PetsConfigHandler;
import me.cable.dynamicpets.handler.PlayerHandler;
import me.cable.dynamicpets.instance.Pet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class PetsMenu implements InventoryHolder {

    private final PetsConfigHandler petsConfigHandler;
    private final PlayerHandler playerHandler;

    private final Player player;

    public PetsMenu(@NotNull Player player) {
        DynamicPets dynamicPets = DynamicPets.getInstance();
        petsConfigHandler = dynamicPets.getPetsConfigHandler();
        playerHandler = dynamicPets.getPlayerHandler();

        this.player = player;
    }

    public void open() {
        player.openInventory(getInventory());
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 9 * 6, "Pets");

        for (Pet pet : playerHandler.getPets(player)) {
            YamlConfiguration config = petsConfigHandler.getPetConfig(pet.getType());

            ItemStack item = new ItemStack(Material.DIAMOND);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(config.getString("name"));
                meta.setLore(config.getStringList("description"));
                item.setItemMeta(meta);
            }
        }

        return inv;
    }

    public void onClick(@NotNull InventoryClickEvent e) {

    }
}
