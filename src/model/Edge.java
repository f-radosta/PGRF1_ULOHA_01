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
        return y1 == y2;
    }

    public void orientate() {
        if (y1 > y2) {
            int tempx = x1;
            int tempy = y1;
            x1 = x2;
            y1 = y2;
            x2 = tempx;
            y2 = tempy;
        }
    }

    public boolean isIntersection(int y) {
        return y2 > y && y >= y1;
    }

    public int getIntersectionX(int y) {
        float t = (y - y1) / (float) (y2 - y1);
        return Math.round(x1 + t * (x2 - x1));
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
