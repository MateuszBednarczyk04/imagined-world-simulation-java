package organisms.plants;

import organisms.Organism;
import game.World;

import java.awt.Color;

public class Dandelion extends Plant {
    public static final String NAME = "Dandelion";
    public static final Color COLOR = Color.YELLOW;

    public Dandelion() {}

    public Dandelion(World world, int x, int y) {
        super(world, x, y, 0);
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
