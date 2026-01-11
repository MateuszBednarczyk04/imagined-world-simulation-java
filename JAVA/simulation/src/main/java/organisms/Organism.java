package organisms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import game.World;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organisms.animals.*;
import organisms.plants.*;

import java.awt.*;
import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Wolf.class, name = "Wolf"),
        @JsonSubTypes.Type(value = Sheep.class, name = "Sheep"),
        @JsonSubTypes.Type(value = Fox.class, name = "Fox"),
        @JsonSubTypes.Type(value = Turtle.class, name = "Turtle"),
        @JsonSubTypes.Type(value = Antelope.class, name = "Antelope"),
        @JsonSubTypes.Type(value = CyberSheep.class, name = "CyberSheep"),
        @JsonSubTypes.Type(value = Human.class, name = "Human"),
        @JsonSubTypes.Type(value = Grass.class, name = "Grass"),
        @JsonSubTypes.Type(value = Dandelion.class, name = "Dandelion"),
        @JsonSubTypes.Type(value = Guarana.class, name = "Guarana"),
        @JsonSubTypes.Type(value = Belladonna.class, name = "Belladonna"),
        @JsonSubTypes.Type(value = Hogweed.class, name = "Hogweed")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class Organism implements Serializable {
    protected int strength;
    protected int initiative;
    protected int x;
    protected int y;
    protected int age;

    @JsonIgnore
    protected World world;

    protected boolean isAlive;

    public Organism(World world, int x, int y, int strength, int initiative) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.initiative = initiative;
        this.age = 0;
        this.isAlive = true;
    }

    public abstract void action();

    public abstract void collision(Organism attacker);

    @JsonIgnore
    public abstract String getName();

    @JsonIgnore
    public abstract Color getColor();

    public void draw() {
        // This might be handled by the GUI, but we can keep it for console representation if needed
    }

    public void incrementAge() {
        this.age++;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @JsonIgnore
    public boolean isImmortal() {
        return false;
    }
}
