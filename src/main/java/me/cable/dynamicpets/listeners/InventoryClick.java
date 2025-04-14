package me.cable.dynamicpets.listeners;

import me.cable.dynamicpets.menu.PetsMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClick implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof PetsMenu petsMenu) {
            petsMenu.onClick(event);
        }
    }
}
