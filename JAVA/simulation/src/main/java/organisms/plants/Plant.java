package organisms.plants;

import game.World;
import organisms.Organism;

import java.awt.*;
import java.util.List;
import java.util.Random;

public abstract class Plant extends Organism {
    protected Random random;
    protected double reproducingProbability = 0.05; // 5% chance

    public Plant() {
        this.random = new Random();
    }

    public Plant(World world, int x, int y, int strength) {
        super(world, x, y, strength, 0);
        this.random = new Random();
    }

    @Override
    public void action() {
        if (random.nextDouble() < reproducingProbability) {
            reproduce();
        }
    }

    protected void reproduce() {
        final List<Point> freeSpots = world.getFreeNeighbors(x, y);
        if (!freeSpots.isEmpty()) {
            final Point p = freeSpots.get(random.nextInt(freeSpots.size()));
            final Organism newPlant = createNewPlant(p.x, p.y);
            world.addOrganism(newPlant);
            world.getLogger().log("New plan grown: " + newPlant.getName() + " on (" + p.x + "," + p.y + ")");
        }
    }

    @Override
    public void collision(final Organism attacker) {
        world.getLogger().log(attacker.getName() + " eats " + this.getName());
        world.removeOrganism(this);
        attacker.setPosition(this.x, this.y);
    }

    protected abstract Organism createNewPlant(final int x, final int y);
}
