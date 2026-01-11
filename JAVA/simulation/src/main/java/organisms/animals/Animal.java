package organisms.animals;

import game.World;
import org.apache.commons.collections4.CollectionUtils;
import organisms.Organism;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class Animal extends Organism {
    protected Random random;

    public Animal() {
        this.random = new Random();
    }

    public Animal(final World world, final int x, final int y, final int strength, final int initiative) {
        super(world, x, y, strength, initiative);
        this.random = new Random();
    }

    @Override
    public void action() {
        final List<Point> neighbors = world.getNeighbors(x, y);
        if (CollectionUtils.isEmpty(neighbors)) return;

        final Point target = neighbors.get(random.nextInt(neighbors.size()));
        final Organism other = world.getOrganismAt(target.x, target.y);

        if (Objects.nonNull(other)) {
            other.collision(this);
        } else {
            this.x = target.x;
            this.y = target.y;
        }
    }

    @Override
    public void collision(final Organism attacker) {
        if (this.getClass().equals(attacker.getClass())) {
            reproduce(attacker);
        } else {
            fight(attacker);
        }
    }

    protected void reproduce(final Organism partner) {
        List<Point> freeSpots = world.getFreeNeighbors(this.x, this.y);
        if (CollectionUtils.isEmpty(freeSpots)) {
            freeSpots = world.getFreeNeighbors(partner.getX(), partner.getY());
        }

        if (CollectionUtils.isNotEmpty(freeSpots)) {
            final Point p = freeSpots.get(random.nextInt(freeSpots.size()));
            final Organism child = createChild(p.x, p.y);
            world.addOrganism(child);
            world.getLogger().log("New " + child.getName() + " on position (" + p.x + "," + p.y + ")");
        }
    }

    protected void fight(final Organism attacker) {
        world.getLogger().log(attacker.getName() + " (strength: " + attacker.getStrength() + ") attacks " +
                this.getName() + " strength: " + this.getStrength() + " on (" + x + "," + y + ")");

        if (this.isImmortal()) {
            world.getLogger().log(this.getName() + " is immortal! Attack defended");
            return;
        }

        if (attacker.isImmortal()) {
            if (attacker.getStrength() >= this.getStrength()) {
                world.getLogger().log(this.getName() + " dies.");
                world.removeOrganism(this);
                attacker.setPosition(this.x, this.y);
            } else {
                world.getLogger().log(attacker.getName() + " is weaker but can't be defeated! Backs off.");
            }
            return;
        }

        if (attacker.getStrength() >= this.getStrength()) {
            world.getLogger().log(this.getName() + " dies.");
            world.removeOrganism(this);
            attacker.setPosition(this.x, this.y);
        } else {
            world.getLogger().log(attacker.getName() + " dies.");
            world.removeOrganism(attacker);
        }
    }

    protected abstract Organism createChild(int x, int y);
}
