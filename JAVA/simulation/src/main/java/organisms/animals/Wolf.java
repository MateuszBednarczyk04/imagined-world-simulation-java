package organisms.animals;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Wolf extends Animal {
    public static final String NAME = "Wolf";
    public static final Color COLOR = Color.GRAY;

    public Wolf(final World world, final int x, final int y) {
        super(world, x, y, 9, 5);
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
        return new Wolf(world, x, y);
    }
}
