package organisms.animals;

import game.World;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import organisms.Organism;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class Antelope extends Animal {
    public static final String NAME = "Antelope";
    public static final Color COLOR = new Color(139, 69, 19);
    private static final int STRENGTH = 4;
    private static final int INITIATIVE = 4;

    public Antelope(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH, INITIATIVE);
    }

    @Override
    public void action() {
        final List<Point> neighbors1 = world.getNeighbors(x, y);
        if (CollectionUtils.isEmpty(neighbors1)) return;

        final Point p1 = neighbors1.get(random.nextInt(neighbors1.size()));

        final List<Point> neighbors2 = world.getNeighbors(p1.x, p1.y);
        if (CollectionUtils.isEmpty(neighbors2)) return;

        final Point target = neighbors2.get(random.nextInt(neighbors2.size()));

        final Organism other = world.getOrganismAt(target.x, target.y);
        if (Objects.nonNull(other)) {
            if (other == this) return;
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
            if (random.nextDouble() < 0.5) {
                final List<Point> freeSpots = world.getFreeNeighbors(this.x, this.y);
                if (CollectionUtils.isNotEmpty(freeSpots)) {
                    final Point escape = freeSpots.get(random.nextInt(freeSpots.size()));
                    world.getLogger().log("Antelope dodges attack from " + attacker.getName() + " on (" + escape.x + "," + escape.y + ") square");

                    this.x = escape.x;
                    this.y = escape.y;

                    return;
                }
            }
            fight(attacker);
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
        return new Antelope(world, x, y);
    }
}
