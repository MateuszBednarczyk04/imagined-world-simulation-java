package organisms.animals;

import game.Constants;
import game.HexWorld;
import game.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisms.Organism;

import java.awt.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Human extends Animal {
    public static final String NAME = "Human";
    public static final Color COLOR = Color.BLUE;
    private static final int STRENGTH = 5;
    private static final int INITIATIVE = 4;
    private static final int MAX_ABILITY_DURATION = 5;
    private static final int MAX_ABILITY_COOLDOWN = 5;

    @Setter
    private int nextMoveDirection = -1;

    private int abilityCooldown = 0;
    private int abilityDuration = 0;
    private boolean abilityActive = false;

    public Human(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH, INITIATIVE);
    }

    public void activateAbility() {
        if (abilityCooldown == 0) {
            abilityActive = true;
            abilityDuration = MAX_ABILITY_DURATION;
            world.getLogger().log("Human activated ability.");
        } else {
            world.getLogger().log("Human's ability unavailable. Cooldown: " + abilityCooldown);
        }
    }

    @Override
    public boolean isImmortal() {
        return abilityActive;
    }

    @Override
    public void action() {
        if (abilityActive) {
            world.getLogger().log("Immortability activated, duration: " + abilityDuration);
            abilityDuration--;
            if (abilityDuration == 0) {
                abilityActive = false;
                abilityCooldown = MAX_ABILITY_COOLDOWN;
                world.getLogger().log("Immortability deactivated. Cooldown: " + abilityCooldown);
            }
        } else if (abilityCooldown > 0) {
            abilityCooldown--;
        }

        if (nextMoveDirection == -1) return;

        int newX = x;
        int newY = y;

        if (world instanceof HexWorld) {
            final int[][] directions = (y % 2 == 0) ? Constants.HEX_DIRECTIONS_EVEN_Y : Constants.HEX_DIRECTIONS_ODD_Y;

            if (nextMoveDirection >= 0 && nextMoveDirection < 6) {
                newX += directions[nextMoveDirection][0];
                newY += directions[nextMoveDirection][1];
            }

        } else {
            if (nextMoveDirection == 0) newY--;
            else if (nextMoveDirection == 1) newY++;
            else if (nextMoveDirection == 2) newX--;
            else if (nextMoveDirection == 3) newX++;
        }

        nextMoveDirection = -1;

        if (newX < 0 || newX >= world.getWidth() || newY < 0 || newY >= world.getHeight()) {
            return;
        }

        final Organism other = world.getOrganismAt(newX, newY);
        if (Objects.nonNull(other)) {
            other.collision(this);
        } else {
            this.x = newX;
            this.y = newY;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    protected Organism createChild(int x, int y) {
        return null;
    }
}
