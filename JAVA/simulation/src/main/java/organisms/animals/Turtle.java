package organisms.animals;

import game.World;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Turtle extends Animal {
    public static final String NAME = "Turtle";
    public static final Color COLOR = new Color(0, 100, 0);
    private static final int STRENGTH = 2;
    private static final int INITIATIVE = 1;
    private static final Double CHANCE_FOR_DEFEND_ATTACK = 0.75;

    public Turtle(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH, INITIATIVE);
    }

    @Override
    public void action() {
        if (random.nextDouble() >= CHANCE_FOR_DEFEND_ATTACK) {
            super.action();
        }
    }

    @Override
    public void collision(final Organism attacker) {
        if (this.getClass().equals(attacker.getClass())) {
            reproduce(attacker);
        } else {
            if (attacker.getStrength() < 5 && attacker instanceof Animal) {
                world.getLogger().log("Turtle defends attack of " + attacker.getName());
            } else {
                fight(attacker);
            }
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
        return new Turtle(world, x, y);
    }


}
