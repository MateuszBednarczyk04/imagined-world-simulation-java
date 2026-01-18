package organisms.animals;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Sheep extends Animal {
    public static final String NAME = "Sheep";
    public static final Color COLOR = Color.LIGHT_GRAY;
    private static final int STRENGTH = 4;
    private static final int INITIATIVE = 4;

    public Sheep(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH, INITIATIVE);
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
        return new Sheep(world, x, y);
    }
}
