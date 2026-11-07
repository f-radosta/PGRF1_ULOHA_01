package model;

public class Edge {
    public int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isHorizontal() {
        // TODO: implementovat
        return true;
    }

    public void orientate() {
        // TODO: implementovat
    }

    public boolean isIntersection(int y) {
        // TODO: implementovat
        return true;
    }

    public int getIntersectionX(int y) {
        // TODO: implementovat
        return 0;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
