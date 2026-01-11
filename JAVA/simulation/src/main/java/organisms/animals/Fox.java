package organisms.animals;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class Fox extends Animal {
    public static final String NAME = "Fox";
    public static final Color COLOR = Color.ORANGE;

    public Fox(final World world, final int x, final int y) {
        super(world, x, y, 3, 7);
    }

    @Override
    public void action() {
        final List<Point> neighbors = world.getNeighbors(x, y);
        Collections.shuffle(neighbors);

        for (final Point p : neighbors) {
            final Organism other = world.getOrganismAt(p.x, p.y);
            if (Objects.isNull(other) || other.getStrength() <= this.getStrength()) {
                if (Objects.nonNull(other)) {
                    other.collision(this);
                } else {
                    this.x = p.x;
                    this.y = p.y;
                }
                return;
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
        return new Fox(world, x, y);
    }
}
