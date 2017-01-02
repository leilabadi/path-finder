package fun.leilabadi.pathfinder;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class Location implements Cloneable<Location> {
    public int i;
    public int j;

    public Location(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public Integer getI() {
        return i;
    }

    public Integer getJ() {
        return j;
    }

    public int distanceFrom(Location target) {
        return Math.abs(i - target.i) + Math.abs(j - target.j);
    }

    @Override
    public Location clone() {
        return new Location(i, j);
    }

    @Override
    public String toString() {
        return "(" + i + "," + j + ')';
    }
}
