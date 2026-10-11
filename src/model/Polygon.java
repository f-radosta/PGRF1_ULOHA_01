package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    List<Point> points;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void addPoint(int index, Point point) {
        points.add(index, point);
    }

    public Point getPoint(int i) {
        return points.get(i);
    }

    public int size() {
        return points.size();
    }

    public void clear() {
        points.clear();;
    }
}
