package organisms.animals;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;
import organisms.plants.Hogweed;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class CyberSheep extends Animal {
    public static final String NAME = "Cyber-sheep";
    public static final Color COLOR = Color.CYAN;

    public CyberSheep(final World world, final int x, final int y) {
        super(world, x, y, 11, 4);
    }

    @Override
    public void action() {
        final List<Organism> organisms = world.getOrganisms();
        Organism nearestHogweed = null;
        double minDistance = Double.MAX_VALUE;

        for (final Organism o : organisms) {
            if (o instanceof Hogweed && o.isAlive()) {
                final double dist = Math.sqrt(Math.pow(this.x - o.getX(), 2) + Math.pow(this.y - o.getY(), 2));
                if (dist < minDistance) {
                    minDistance = dist;
                    nearestHogweed = o;
                }
            }
        }

        if (Objects.nonNull(nearestHogweed)) {
            final List<Point> neighbors = world.getNeighbors(x, y);
            Point bestMove = null;
            double bestDist = minDistance;

            for (final Point p : neighbors) {
                double dist = Math.sqrt(Math.pow(p.x - nearestHogweed.getX(), 2) + Math.pow(p.y - nearestHogweed.getY(), 2));
                if (dist < bestDist) {
                    bestDist = dist;
                    bestMove = p;
                }
            }

            if (Objects.nonNull(bestMove)) {
                final Organism other = world.getOrganismAt(bestMove.x, bestMove.y);
                if (Objects.nonNull(other)) {
                    other.collision(this);
                } else {
                    this.x = bestMove.x;
                    this.y = bestMove.y;
                }
            }
        } else {
            super.action();
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
        return new CyberSheep(world, x, y);
    }
}
