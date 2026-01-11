package game;

import gui.GameGUI;
import organisms.animals.*;
import organisms.plants.*;

import javax.swing.*;
import java.awt.*;

public class GameInitializer {

    public static void startNewGame() {
        final String[] worldTypeOptions = {"Square", "Hex"};
        final int typeChoice = JOptionPane.showOptionDialog(null, "Choose world type", "World type selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, worldTypeOptions, worldTypeOptions[0]);

        final JTextField widthField = new JTextField(String.valueOf(Constants.DEFAULT_MAP_SIZE));
        final JTextField heightField = new JTextField(String.valueOf(Constants.DEFAULT_MAP_SIZE));
        final Object[] message = {
                "Width:", widthField,
                "Height:", heightField
        };

        final int option = JOptionPane.showConfirmDialog(null, message, "Enter map size", JOptionPane.OK_CANCEL_OPTION);

        int width = Constants.DEFAULT_MAP_SIZE;
        int height = Constants.DEFAULT_MAP_SIZE;

        if (option == JOptionPane.OK_OPTION) {
            try {
                width = Integer.parseInt(widthField.getText());
                height = Integer.parseInt(heightField.getText());
            } catch (NumberFormatException ex) {
                width = Constants.DEFAULT_MAP_SIZE;
                height = Constants.DEFAULT_MAP_SIZE;
            }
        }

        final World world = typeChoice == 1 ? new HexWorld(width, height) : new SquareWorld(width, height);

        world.addOrganisms(Wolf.class, 4);
        world.addOrganisms(Sheep.class, 6);
        world.addOrganisms(Fox.class, 4);
        world.addOrganisms(Turtle.class, 4);
        world.addOrganisms(Antelope.class, 4);
        world.addOrganisms(CyberSheep.class, 4);
        world.addOrganisms(Grass.class, 8);
        world.addOrganisms(Dandelion.class, 6);
        world.addOrganisms(Guarana.class, 2);
        world.addOrganisms(Belladonna.class, 4);
        world.addOrganisms(Hogweed.class, 5);

        final Point p = world.getRandomFreePosition();
        final Human human = new Human(world, p.x, p.y);
        world.setHuman(human);

        new GameGUI(world);
    }
}
