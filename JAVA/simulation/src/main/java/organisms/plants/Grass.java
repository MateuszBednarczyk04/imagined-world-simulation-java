package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;

import java.awt.*;

@NoArgsConstructor
public class Grass extends Plant {
    public static final String NAME = "Grass";
    public static final Color COLOR = Color.GREEN;
    private static final int STRENGTH = 0;

    public Grass(World world, int x, int y) {
        super(world, x, y, STRENGTH);
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
        return new Grass(world, x, y);
    }
}
