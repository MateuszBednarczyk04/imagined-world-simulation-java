package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisms.Organism;
import organisms.animals.Human;
import utils.ObjectMapperCreator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "worldType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SquareWorld.class, name = "SquareWorld"),
        @JsonSubTypes.Type(value = HexWorld.class, name = "HexWorld")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class World implements Serializable {
    protected int width;
    protected int height;
    protected int turnNumber;
    protected List<Organism> organisms = new ArrayList<>();

    @JsonIgnore
    protected List<Organism> newOrganisms = new ArrayList<>();

    @JsonIgnore
    protected transient GameLogger logger = new GameLogger();

    @JsonIgnore
    protected Human human;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.turnNumber = 0;
        this.organisms = new ArrayList<>();
        this.newOrganisms = new ArrayList<>();
        this.logger = new GameLogger();
    }

    public abstract List<Point> getNeighbors(final int x, final int y);

    public List<Point> getFreeNeighbors(final int x, final int y) {
        final List<Point> neighbors = getNeighbors(x, y);
        final List<Point> freeNeighbors = new ArrayList<>();
        for (Point p : neighbors) {
            if (!isOccupied(p.x, p.y)) {
                freeNeighbors.add(p);
            }
        }
        return freeNeighbors;
    }

    public void makeTurn() {
        turnNumber++;
        getLogger().log("--- Turn " + turnNumber + " ---");

        organisms.sort((o1, o2) -> {
            if (o1.getInitiative() != o2.getInitiative()) {
                return Integer.compare(o2.getInitiative(), o1.getInitiative());
            } else {
                return Integer.compare(o2.getAge(), o1.getAge());
            }
        });

        organisms.stream().filter(Organism::isAlive).forEach(organism -> {
            organism.action();
            organism.incrementAge();
        });

        organisms.removeIf(o -> !o.isAlive());

        organisms.addAll(newOrganisms);
        newOrganisms.clear();
    }

    public void addOrganism(final Organism organism) {
        newOrganisms.add(organism);
    }

    public void addOrganismImmediately(final Organism organism) {
        organisms.add(organism);
    }

    public void addOrganisms(final Class<? extends Organism> organismClass, final int count) {
        for (int i = 0; i < count; i++) {
            final Point p = getRandomFreePosition();
            try {
                final Organism newOrg = organismClass.getDeclaredConstructor(World.class, int.class, int.class)
                        .newInstance(this, p.x, p.y);
                addOrganismImmediately(newOrg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Organism getOrganismAt(final int x, final int y) {
        for (Organism o : organisms) {
            if (o.isAlive() && o.getX() == x && o.getY() == y) {
                return o;
            }
        }
        for (final Organism o : newOrganisms) {
            if (o.isAlive() && o.getX() == x && o.getY() == y) {
                return o;
            }
        }
        return null;
    }

    public boolean isOccupied(final int x, final int y) {
        return getOrganismAt(x, y) != null;
    }

    public void removeOrganism(final Organism organism) {
        organism.setAlive(false);
    }

    @JsonIgnore
    public GameLogger getLogger() {
        if (logger == null) {
            logger = new GameLogger();
        }
        return logger;
    }

    @JsonIgnore
    public Human getHuman() {
        if (human == null) {
            for (final Organism o : organisms) {
                if (o instanceof Human) {
                    human = (Human) o;
                    break;
                }
            }
        }
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
        addOrganismImmediately(human);
    }

    public void saveWorldToFile(final String filename) throws IOException {
        ObjectMapperCreator.getObjectMapper().writeValue(new File(filename), this);
    }

    public World loadWorldFromFile(final String filename) throws IOException {
        final World world = ObjectMapperCreator.getObjectMapper().readValue(new File(filename), World.class);
        for (final Organism o : world.getOrganisms()) {
            o.setWorld(world);
        }

        world.newOrganisms = new ArrayList<>();
        world.logger = new GameLogger();

        return world;
    }

    @JsonIgnore
    public Point getRandomFreePosition() {
        final var rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
        } while (isOccupied(x, y));
        return new Point(x, y);
    }
}
