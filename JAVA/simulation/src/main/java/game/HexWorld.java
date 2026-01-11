package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HexWorld extends World {

    public HexWorld() {
        super();
    }

    public HexWorld(int width, int height) {
        super(width, height);
    }

    @Override
    public List<Point> getNeighbors(int x, int y) {
        final List<Point> neighbors = new ArrayList<>();

        for (int[] dir : y % 2 == 0 ? Constants.HEX_DIRECTIONS_EVEN_Y : Constants.HEX_DIRECTIONS_ODD_Y) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                neighbors.add(new Point(nx, ny));
            }
        }
        return neighbors;
    }
}
