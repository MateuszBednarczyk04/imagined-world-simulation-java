package organisms.plants;

import game.World;
import lombok.NoArgsConstructor;
import organisms.Organism;
import organisms.animals.Animal;
import organisms.animals.CyberSheep;

import java.awt.*;
import java.util.List;

@NoArgsConstructor
public class Hogweed extends Plant {
    public static final String NAME = "Sosnowsky hogweed";
    public static final Color COLOR = new Color(128, 128, 0);

    public Hogweed(World world, int x, int y) {
        super(world, x, y, 10);
    }

    @Override
    public void action() {
        List<Point> neighbors = world.getNeighbors(x, y);
        for (Point p : neighbors) {
            Organism neighbor = world.getOrganismAt(p.x, p.y);
            if (neighbor instanceof Animal && !(neighbor instanceof CyberSheep)) {
                world.getLogger().log(NAME + " kills " + neighbor.getName() + " in neighbourhood.");
                world.removeOrganism(neighbor);
            }
        }
        super.action();
    }

    @Override
    public void collision(Organism attacker) {
        if (attacker instanceof CyberSheep) {
            world.getLogger().log(CyberSheep.NAME + "eats  " + NAME);
            world.removeOrganism(this);
            attacker.setPosition(this.x, this.y);
        } else {
            world.getLogger().log(attacker.getName() + " eats " + NAME + " and dies.");
            world.removeOrganism(attacker);
            world.removeOrganism(this);
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
        return new Hogweed(world, x, y);
    }
}
