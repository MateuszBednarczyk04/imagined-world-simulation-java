package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;
import organisms.animals.Animal;
import organisms.animals.CyberSheep;

import java.awt.*;
import java.util.List;

@NoArgsConstructor
public class Hogweed extends Plant {
    public static final String NAME = "Sosnowsky hogweed";
    public static final Color COLOR = new Color(128, 128, 0);
    private static final int STRENGTH = 10;

    public Hogweed(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH);
    }

    @Override
    public void action() {
        final List<Point> neighbors = world.getNeighbors(x, y);
        for (final Point p : neighbors) {
            final Organism neighbor = world.getOrganismAt(p.x, p.y);
            if (neighbor instanceof Animal && !(neighbor instanceof CyberSheep)) {
                world.getLogger().log(NAME + " kills " + neighbor.getName() + " in neighbourhood.");
                world.removeOrganism(neighbor);
            }
        }
        super.action();
    }

    @Override
    public void collision(final Organism attacker) {
        if (attacker instanceof CyberSheep) {
            world.getLogger().log(CyberSheep.NAME + "eats  " + NAME);
            world.removeOrganism(this);
            attacker.setPosition(this.x, this.y);
        } else {
            world.getLogger().log(attacker.getName() + " eats " + NAME + " and dies.");
            world.removeOrganism(attacker);
            world.removeOrganism(this);
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
    protected Organism createNewPlant(int x, int y) {
        return new Hogweed(world, x, y);
    }
}
