package model;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class Edge {
    public int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Edge(Point2D p1, Point2D p2) {
        this.x1 = p1.getX();
        this.y1 = p1.getY();
        this.x2 = p2.getX();
        this.y2 = p2.getY();
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

    public Point2D getShiftModeLineEnd() {
        int startX = x1;
        int startY = y1;
        int endX = x2;
        int endY = y2;
        Point2D endPoint = new Point2D(endX, endY);
        int relativeX = x2 - x1;
        int relativeY = y2 - y1;
        int r = Math.toIntExact(round(sqrt(relativeX * relativeX + relativeY * relativeY)));
        int a = Math.toIntExact(round((r / sqrt(2))));

        Point2D top = new Point2D(startX, startY - r);
        Point2D topLeft = new Point2D(startX - a, startY - a);
        Point2D topRight = new Point2D(startX + a, startY - a);
        Point2D bottom = new Point2D(startX, startY + r);
        Point2D bottomLeft = new Point2D(startX - a, startY + a);
        Point2D bottomRight = new Point2D(startX + a, startY + a);
        Point2D left = new Point2D(startX - r, startY);
        Point2D right = new Point2D(startX + r, startY);

        if (relativeX > 0) {
            if (relativeY > 0) { // BR
                if (isAcloserThanB(bottom, bottomRight, endPoint)) {
                    return bottom;
                } else if (isAcloserThanB(right, bottomRight, endPoint)) {
                    return right;
                } else {
                    return bottomRight;
                }
            } else { // TR
                if (isAcloserThanB(top, topRight, endPoint)) {
                    return top;
                } else if (isAcloserThanB(right, topRight, endPoint)) {
                    return right;
                } else {
                    return topRight;
                }
            }
        } else {
            if (relativeY > 0) { // BL
                if (isAcloserThanB(bottom, bottomLeft, endPoint)) {
                    return bottom;
                } else if (isAcloserThanB(left, bottomLeft, endPoint)) {
                    return left;
                } else {
                    return bottomLeft;
                }
            } else { // TL
                if (isAcloserThanB(top, topLeft, endPoint)) {
                    return top;
                } else if (isAcloserThanB(left, topLeft, endPoint)) {
                    return left;
                } else {
                    return topLeft;
                }
            }
        }
    }

    private boolean isAcloserThanB(Point2D A, Point2D B, Point2D C) {
        int relACX = A.getX() - C.getX();
        int relACY = A.getY() - C.getY();
        int relBCX = B.getX() - C.getX();
        int relBCY = B.getY() - C.getY();
        if ((relACX*relACX + relACY*relACY) < (relBCX*relBCX + relBCY*relBCY))
            return true;
        return false;
    }
}
