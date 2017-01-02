package fun.leilabadi.pacman;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.GridMap;
import fun.leilabadi.pathfinder.Location;
import fun.leilabadi.pathfinder.Size;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class PacmanGrid extends GridMap<PacmanCell> {

    public PacmanGrid(Size size, Location startLocation, Location goalLocation, PacmanCell[][] cells) {
        super(size, startLocation, goalLocation, convert(size, cells));
    }

    private static Cell[][] convert(Size size, PacmanCell[][] pacmanCells) {
        Cell[][] result = new Cell[size.getRows()][size.getColumns()];
        for (int i = 0; i < size.getRows(); i++) {
            for (int j = 0; j < size.getColumns(); j++) {
                result[i][j] = new Cell(pacmanCells[i][j]);
            }
        }
        return result;
    }
}
