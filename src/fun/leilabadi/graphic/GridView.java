package fun.leilabadi.graphic;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class GridView {
    private final int rows;
    private final int columns;
    private final GridCell[][] cells;

    public GridView(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        cells = new GridCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = new GridCell(row, column);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public GridCell getCell(int row, int column) {
        if (((0 <= row) && (row < rows)) && ((0 <= column) && (column < columns)))
            return cells[row][column];
        return null;
    }
}
