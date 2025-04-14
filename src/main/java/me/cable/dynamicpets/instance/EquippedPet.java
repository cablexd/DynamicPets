package me.cable.dynamicpets.instance;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.handler.PetsConfigHandler;
import me.cable.dynamicpets.instance.movement.Movement;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class EquippedPet {

    private final Pet pet;
    private final Player owner;
    private final EntityDisplay entityDisplay;
    private final List<Movement> movements = new ArrayList<>();

    private final Random random = new Random();
    private @Nullable Movement currentMovement;
    private int movementCooldown;

    public EquippedPet(@NotNull Pet pet, @NotNull Player owner) {
        this.pet = pet;
        this.owner = owner;
        entityDisplay = new EntityDisplay();
        setup();
    }

    private void setup() {
        PetsConfigHandler petsConfigHandler = DynamicPets.getInstance().getPetsConfigHandler();
        Location loc = owner.getLocation();

        entityDisplay.updateData(a -> {
            a.setInvisible(true);

            if (petsConfigHandler.getPetConfig(pet.getType()).getBoolean("small")) {
                a.setSmall(true);
            }
        });
        entityDisplay.updateEquipment(a -> a.setHelmet(petsConfigHandler.getHead(pet.getType())));
        entityDisplay.setPosition(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw());
        entityDisplay.setWorld(owner.getWorld());
    }

    public @NotNull YamlConfiguration getSettings() {
        return DynamicPets.getInstance().getPetsConfigHandler().getPetConfig(pet.getType());
    }

    public @NotNull Pet getPet() {
        return pet;
    }

    public @NotNull EntityDisplay getEntityDisplay() {
        return entityDisplay;
    }

    public @NotNull Player getOwner() {
        return owner;
    }

    public void addMovement(@NotNull Movement movement) {
        movements.add(movement);
    }

    public @NotNull List<Movement> getMovements() {
        return List.copyOf(movements);
    }

    public @Nullable Movement getCurrentMovement() {
        return currentMovement;
    }

    public void setCurrentMovement(@Nullable Movement currentMovement) {
        this.currentMovement = currentMovement;
        resetMovementCooldown();
    }

    public boolean isMovementReady() {
        if (movementCooldown > 0) movementCooldown--;
        return movementCooldown <= 0;
    }

    private void resetMovementCooldown() {
        int min = getSettings().getInt("movement-cooldown.min");
        int max = getSettings().getInt("movement-cooldown.max");
        movementCooldown = random.nextInt(min, max + 1);
    }
}
