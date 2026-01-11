package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Belladonna extends Plant {
    public static final String NAME = "Belladonna";
    public static final Color COLOR = new Color(75, 0, 130);

    public Belladonna(World world, int x, int y) {
        super(world, x, y, 99);
    }

    @Override
    public void collision(Organism attacker) {
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
