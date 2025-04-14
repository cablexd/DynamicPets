package me.cable.dynamicpets.handler;

import me.cable.dynamicpets.DynamicPets;
import me.cable.dynamicpets.instance.EquippedPet;
import me.cable.dynamicpets.instance.movement.Movement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovementHandler {

    private final DynamicPets dynamicPets;
    private final PlayerHandler playerHandler;

    public MovementHandler(@NotNull DynamicPets dynamicPets) {
        this.dynamicPets = dynamicPets;
        playerHandler = dynamicPets.getPlayerHandler();
    }

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(dynamicPets, this::tick, 0, 1);
    }

    private void startMovement(@NotNull EquippedPet equippedPet, @NotNull Movement movement) {
        equippedPet.setCurrentMovement(movement);
        movement.start();
    }

    private void equippedPetTick(@NotNull EquippedPet equippedPet) {
        List<Movement> allMovements = equippedPet.getMovements();
        Movement currentMovement = equippedPet.getCurrentMovement();

        for (Movement movement : allMovements) {
            if (movement.override() && (currentMovement == null || currentMovement.getClass() != movement.getClass())) {
                if (currentMovement != null) {
                    currentMovement.onEnd();
                }

                startMovement(equippedPet, movement);
                return;
            }
        }

        if (currentMovement == null && equippedPet.isMovementReady()) {
            List<Movement> availableMovements = allMovements.stream().filter(Movement::selectRandomly).toList();

            if (!availableMovements.isEmpty()) {
                Movement movement = availableMovements.get((int) (Math.random() * availableMovements.size()));
                startMovement(equippedPet, movement);
            }
        }
    }

    private void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (EquippedPet equippedPet : playerHandler.getEquippedPets(player)) {
                equippedPetTick(equippedPet);
            }
        }
    }
}
