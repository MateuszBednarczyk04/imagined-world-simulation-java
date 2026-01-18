package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Dandelion extends Plant {
    public static final String NAME = "Dandelion";
    public static final Color COLOR = Color.YELLOW;
    private static final int STRENGTH = 0;

    public Dandelion(final World world, final int x, final int y) {
        super(world, x, y, STRENGTH);
    }

    @Override
    public void action() {
        for (int i = 0; i < 3; i++) {
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
    protected Organism createNewPlant(int x, int y) {
        return new Dandelion(world, x, y);
    }
}
