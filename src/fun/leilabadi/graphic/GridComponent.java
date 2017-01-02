package fun.leilabadi.graphic;

import fun.leilabadi.pacman.PacmanCell;
import fun.leilabadi.pacman.PacmanGrid;
import fun.leilabadi.pacman.PacmanSolution;
import fun.leilabadi.pathfinder.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class GridComponent extends JComponent {
    private final GridView gridView;
    private final PacmanGrid gridMap;
    private BufferedImage buffer;
    private double cellWidth;
    private double cellHeight;
    private Color defaultColor;
    private Paint defaultPaint;
    private Stroke defaultStroke;
    private GridCell dragTarget;
    private boolean selectionFlag;

    public GridComponent(PacmanGrid gridMap) {
        this.gridMap = gridMap;
        gridView = new GridView(gridMap.getSize().getRows(), gridMap.getSize().getColumns());
        setCellSize();
        initGrid();

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                GridCell cell = getPassingCell(e.getPoint());
                if ((cell != null) && (cell != dragTarget)) {
                    dragTarget = cell;
                    dragTarget.setSelected(selectionFlag);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                GridCell cell = getPassingCell(e.getPoint());
                if (cell != null) {
                    dragTarget = cell;
                    selectionFlag = !dragTarget.isSelected();
                    dragTarget.setSelected(selectionFlag);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragTarget = null;
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                int key = e.getKeyCode();

                return false;
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                buffer = null;
                setCellSize();
                initGrid();
            }
        });

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null)
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) buffer.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        saveGraphicState(g2);
        drawGrid(g2);

        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }


    private void initGrid() {
        Point2D.Double point1, point2;
        GridCell cell;
        for (int i = 0; i < gridView.getRows(); i++) {
            for (int j = 0; j < gridView.getColumns(); j++) {
                cell = gridView.getCell(i, j);
                point1 = new Point2D.Double(i * cellWidth, j * cellHeight);
                point2 = new Point2D.Double(point1.getX() + cellWidth, point1.getY() + cellHeight);
                cell.setLocation(point1);
                cell.setOriginalPaint(new GradientPaint(point1, new Color(200, 200, 200), point2, new Color(223, 223, 223)));
                cell.setSelectedPaint(new Color(128, 128, 128));

                if (gridMap.getCell(i, j).getValue() == PacmanCell.WALL)
                    cell.setSelected(true);

                switch (gridMap.getCell(i, j).getValue()) {
                    case WALL:
                        cell.setSelected(true);
                        break;
                    case PACMAN:
                        cell.setOverridePaint(new Color(0, 0, 0));
                        break;
                    case FOOD:
                        cell.setOverridePaint(new Color(0, 255, 0));
                        break;
                }
            }
        }
    }

    private void drawGrid(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        Rectangle2D.Double mainGrid = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        graphics.draw(mainGrid);
        restoreGraphicState(graphics);

        graphics.setColor(Color.black);
        Line2D line;
        for (double i = 0; i < getHeight(); i += getHeight() / (double) gridView.getRows()) {
            line = new Line2D.Double(0, i, getWidth(), i);
            graphics.draw(line);
        }
        for (double i = 0; i < getWidth(); i += getWidth() / (double) gridView.getColumns()) {
            line = new Line2D.Double(i, 0, i, getHeight());
            graphics.draw(line);
        }
        restoreGraphicState(graphics);

        Paint paint;
        GridCell cell;
        Rectangle2D.Double rectangle;
        for (int i = 0; i < gridView.getRows(); i++) {
            for (int j = 0; j < gridView.getColumns(); j++) {
                cell = gridView.getCell(i, j);
                paint = cell.getOverridePaint() != null ? cell.getOverridePaint() :
                        (cell.isSelected() ? cell.getSelectedPaint() : cell.getOriginalPaint());
                graphics.setPaint(paint);
                rectangle = new Rectangle2D.Double(cell.getLocation().getX(), cell.getLocation().getY(), cellWidth - 1, cellHeight - 1);
                graphics.fill(rectangle);
            }
        }
        restoreGraphicState(graphics);
    }


    private void setCellSize() {
        cellWidth = getWidth() / (double) gridView.getColumns();
        cellHeight = getHeight() / (double) gridView.getRows();
    }

    private void saveGraphicState(Graphics2D graphics) {
        defaultColor = graphics.getColor();
        defaultPaint = graphics.getPaint();
        defaultStroke = graphics.getStroke();
    }

    private void restoreGraphicState(Graphics2D graphics) {
        graphics.setColor(defaultColor);
        graphics.setPaint(defaultPaint);
        graphics.setStroke(defaultStroke);
    }

    private GridCell getPassingCell(Point point) {
        int i = (int) (point.getX() / cellWidth);
        int j = (int) (point.getY() / cellHeight);
        return gridView.getCell(i, j);
    }

    public void update(PacmanSolution solution) {
        GridCell cell;
        for (State state : solution.getVisitedNodes()) {
            cell = gridView.getCell(state.getCurrentLocation().getI(), state.getCurrentLocation().getJ());
            if ((state.getCurrentLocation().distanceFrom(gridMap.getStartLocation()) != 0)
                    && (state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) != 0))
                cell.setOverridePaint(new Color(255, 249, 111));
        }

        for (State state : solution.getExploredNodes()) {
            cell = gridView.getCell(state.getCurrentLocation().getI(), state.getCurrentLocation().getJ());
            if ((state.getCurrentLocation().distanceFrom(gridMap.getStartLocation()) != 0)
                    && (state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) != 0))
                cell.setOverridePaint(new Color(255, 152, 35));
        }

        repaint();
    }

    public void highlightPath(List<State> path) {
        GridCell cell;
        for (State state : path) {
            cell = gridView.getCell(state.getCurrentLocation().getI(), state.getCurrentLocation().getJ());
            if ((state.getCurrentLocation().distanceFrom(gridMap.getStartLocation()) != 0)
                    && (state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) != 0))
                cell.setOverridePaint(new Color(0, 0, 255));
        }

        repaint();
    }
}
