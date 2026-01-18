package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Guarana extends Plant {
    public static final String NAME = "Guarana";
    public static final Color COLOR = Color.RED;
    private static final int STRENGTH = 0;

    public Guarana(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH);
    }

    @Override
    public void collision(final Organism attacker) {
        attacker.setStrength(attacker.getStrength() + 3);
        world.getLogger().log(attacker.getName() + " eats Guarana and gains +3 strength.");
        super.collision(attacker);
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
    protected Organism createNewPlant(int x, int y) {
        return new Guarana(world, x, y);
    }
}
