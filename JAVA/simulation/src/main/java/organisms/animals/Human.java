package organisms.animals;

import game.HexWorld;
import game.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisms.Organism;

import java.awt.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Human extends Animal {
    public static final String NAME = "Human";
    public static final Color COLOR = Color.BLUE;

    @Setter
    private int nextMoveDirection = -1;

    private int abilityCooldown = 0;
    private int abilityDuration = 0;
    private boolean abilityActive = false;

    public Human(final World world, final int x, final int y) {
        super(world, x, y, 5, 4);
    }

    public void activateAbility() {
        if (abilityCooldown == 0) {
            abilityActive = true;
            abilityDuration = 5;
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
                abilityCooldown = 5;
                world.getLogger().log("Immortability deactivated. Cooldown: " + abilityCooldown);
            }
        } else if (abilityCooldown > 0) {
            abilityCooldown--;
        }

        if (nextMoveDirection == -1) return;

        int newX = x;
        int newY = y;

        if (world instanceof HexWorld) {
            int[][] directions;
            if (y % 2 == 0) {
                directions = new int[][]{
                        {-1, -1}, {0, -1},
                        {-1, 0}, {1, 0},
                        {-1, 1}, {0, 1}
                };
            } else {
                directions = new int[][]{
                        {0, -1}, {1, -1},
                        {-1, 0}, {1, 0},
                        {0, 1}, {1, 1}
                };
            }

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

        Organism other = world.getOrganismAt(newX, newY);
        if (other != null) {
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
