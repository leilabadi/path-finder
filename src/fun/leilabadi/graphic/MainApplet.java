package fun.leilabadi.graphic;

import fun.leilabadi.pacman.PacmanCell;
import fun.leilabadi.pacman.PacmanSolution;
import fun.leilabadi.pathfinder.Location;
import fun.leilabadi.pathfinder.Size;
import fun.leilabadi.pathfinder.State;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class MainApplet extends JApplet {
    private static final int CELL_SIZE = 15;
    private final String data = "35 35\n" +
            "35 1\n" +
            "37 37\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "%-------%-%-%-----------%---%-----%-%\n" +
            "%-%%%%%%%-%-%%%-%-%%%-%%%-%%%%%%%-%-%\n" +
            "%-------%-------%-%-----%-----%-%---%\n" +
            "%%%%%-%%%%%-%%%-%-%-%-%%%-%%%%%-%-%%%\n" +
            "%---%-%-%-%---%-%-%-%---%-%---%-%---%\n" +
            "%-%%%-%-%-%-%%%-%%%%%-%%%-%-%%%-%%%-%\n" +
            "%-------%-----%---%---%-----%-%-%---%\n" +
            "%%%-%%%%%%%%%-%%%%%%%-%%%-%%%-%-%-%-%\n" +
            "%-------------%-------%-%---%-----%-%\n" +
            "%-%-%%%%%-%-%%%-%-%-%%%-%-%%%-%%%-%-%\n" +
            "%-%-%-----%-%-%-%-%-----%---%-%-%-%-%\n" +
            "%-%-%-%%%%%%%-%-%%%%%%%%%-%%%-%-%%%-%\n" +
            "%-%-%-%-----%---%-----%-----%---%---%\n" +
            "%%%-%%%-%-%%%%%-%%%%%-%%%-%%%-%%%%%-%\n" +
            "%-----%-%-%-----%-%-----%-%---%-%-%-%\n" +
            "%-%-%-%-%-%%%-%%%-%%%-%%%-%-%-%-%-%-%\n" +
            "%-%-%-%-%-----------------%-%-%-----%\n" +
            "%%%-%%%%%%%-%-%-%%%%%-%%%-%-%%%-%%%%%\n" +
            "%-------%-%-%-%-----%---%-----%-%---%\n" +
            "%%%%%-%-%-%%%%%%%%%-%%%%%%%%%%%-%-%%%\n" +
            "%---%-%-----------%-%-----%---%-%---%\n" +
            "%-%%%-%%%%%-%%%%%%%%%-%%%%%-%-%-%%%-%\n" +
            "%-%---%------%--------%-----%-------%\n" +
            "%-%-%-%%%%%-%%%-%-%-%-%-%%%%%%%%%%%%%\n" +
            "%-%-%---%-----%-%-%-%-------%---%-%-%\n" +
            "%-%-%%%-%%%-%-%-%-%%%%%%%%%-%%%-%-%-%\n" +
            "%-%---%-%---%-%-%---%-%---%-%-%-----%\n" +
            "%-%%%-%%%-%%%%%-%%%-%-%-%%%%%-%-%%%%%\n" +
            "%-------%---%-----%-%-----%---%-%---%\n" +
            "%%%-%-%%%%%-%%%%%-%%%-%%%-%-%%%-%-%%%\n" +
            "%-%-%-%-%-%-%-%-----%-%---%-%---%-%-%\n" +
            "%-%-%%%-%-%-%-%-%%%%%%%%%-%-%-%-%-%-%\n" +
            "%---%---%---%-----------------%-----%\n" +
            "%-%-%-%-%%%-%%%-%%%%%%%-%%%-%%%-%%%-%\n" +
            "%.%-%-%-------%---%-------%---%-%--P%\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n";
    private GridComponent component;
    private Thread thread;

    @Override
    public void init() {
        int index = 0;
        String[] lines, strArray;

        lines = data.split("\n");

        strArray = lines[index++].split(" ");
        int pacmanX = Integer.parseInt(strArray[0]);
        int pacmanY = Integer.parseInt(strArray[1]);

        strArray = lines[index++].split(" ");
        int foodX = Integer.parseInt(strArray[0]);
        int foodY = Integer.parseInt(strArray[1]);

        strArray = lines[index++].split(" ");
        int rows = Integer.parseInt(strArray[0]);
        int columns = Integer.parseInt(strArray[1]);

        String gridString[] = new String[rows];

        for (int i = 0; i < rows; i++) {
            gridString[i] = lines[index++];
        }

        PacmanCell[][] cells = new PacmanCell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = PacmanCell.fromSymbol(gridString[i].charAt(j));
            }
        }

        Size gridSize = new Size(rows, columns);
        Location startLocation = new Location(pacmanX, pacmanY);
        Location goalLocation = new Location(foodX, foodY);
        PacmanSolution pacmanSolution = new PacmanSolution(gridSize, startLocation, goalLocation, cells, e -> {
            component.update((PacmanSolution) e.getSource());
            try {
                Thread.sleep(20);
            } catch (InterruptedException ignored) {
            }
        });

        component = new GridComponent(pacmanSolution.getGridMap());
        setSize(gridSize.getColumns() * CELL_SIZE, gridSize.getRows() * CELL_SIZE);
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);

        thread = new Thread(() -> {
            List<State> path = pacmanSolution.calculatePath();
            component.highlightPath(path);
        });
        thread.start();
    }

    @Override
    public void destroy() {
        if (thread != null)
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        thread = null;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
