package game;

import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class Constants {

    public static final int WINDOW_WIDTH = 1600;
    public static final int WINDOW_HEIGHT = 900;
    public static final String FONT_NAME = Font.SANS_SERIF;

    public static final int DEFAULT_MAP_SIZE = 20;

    public static final int[][] HEX_DIRECTIONS_EVEN_Y = {
            {-1, -1}, {0, -1},
            {-1, 0}, {1, 0},
            {-1, 1}, {0, 1}
    };

    public static final int[][] HEX_DIRECTIONS_ODD_Y = {
            {0, -1}, {1, -1},
            {-1, 0}, {1, 0},
            {0, 1}, {1, 1}
    };

    public static final int[][] SQUARE_DIRECTIONS = {
            {0, -1}, {0, 1}, {-1, 0}, {1, 0}
    };

}
