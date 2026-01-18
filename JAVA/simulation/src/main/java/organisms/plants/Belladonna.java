package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Belladonna extends Plant {
    public static final String NAME = "Belladonna";
    public static final Color COLOR = new Color(75, 0, 130);
    private static final int STRENGTH = 99;

    public Belladonna(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH);
    }

    @Override
    public void collision(final Organism attacker) {
        world.getLogger().log(attacker.getName() + " eats Belladonna and dies.");
        world.removeOrganism(attacker);
        world.removeOrganism(this);
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
        return new Belladonna(world, x, y);
    }
}
