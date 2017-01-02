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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new GridCell(i, j);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public GridCell getCell(int i, int j) {
        if (((0 <= i) && (i < rows)) && ((0 <= j) && (j < columns)))
            return cells[i][j];
        return null;
    }
}
