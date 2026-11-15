package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point2D> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(Point2D point) {
        points.add(point);
    }

    public void clear() {
        points.clear();
    }

    public Point2D getPoint(int i) {
        return points.get(i);
    }

    public int getSize() {
        return points.size();
    }

    public void clip(Polygon clipper) {
        if (getSize() < 3)
            return;

        List<Edge> clipperEdges = getEdges(clipper);

        long orientation = 0;
        for (Edge clipperEdge : clipperEdges) {
            orientation += (long) clipperEdge.x1 * clipperEdge.y2 - (long) clipperEdge.x2 * clipperEdge.y1;
        }
        if (orientation == 0)
            return;

        for (Edge clipperEdge : clipperEdges) {
            List<Edge> polygonEdges = getEdges(this);
            List<Point2D> newPoints = new ArrayList<>();

            for (Edge polygonEdge : polygonEdges) {
                Point2D polygonEdgePointA = new Point2D(polygonEdge.getX1(), polygonEdge.getY1());
                Point2D polygonEdgePointB = new Point2D(polygonEdge.getX2(), polygonEdge.getY2());
                Point2D clipperEdgePointA = new Point2D(clipperEdge.getX1(), clipperEdge.getY1());
                Point2D clipperEdgePointB = new Point2D(clipperEdge.getX2(), clipperEdge.getY2());

                // Tečný vektor
                int tx = clipperEdgePointB.getX() - clipperEdgePointA.getX();
                int ty = clipperEdgePointB.getY() - clipperEdgePointA.getY();
                // Normála - normálový vektor
                int nx = -ty;
                int ny = tx;
                // Vektor v - k bodu A
                int acx = polygonEdgePointA.getX() - clipperEdgePointA.getX();
                int acy = polygonEdgePointA.getY() - clipperEdgePointA.getY();
                // Vektor v - k bodu B
                int bcx = polygonEdgePointB.getX() - clipperEdgePointA.getX();
                int bcy = polygonEdgePointB.getY() - clipperEdgePointA.getY();
                // Skalární součin
                float dotA = nx * acx + ny * acy;
                float dotB = nx * bcx + ny * bcy;

                boolean aInside = orientation > 0 ? dotA >= 0 : dotA <= 0;
                boolean bInside = orientation > 0 ? dotB >= 0 : dotB <= 0;

                // Oba vrcholy uvnitř – do výsledku patří jen konec hrany
                if (aInside && bInside) {
                    newPoints.add(polygonEdgePointB);
                // Odchází ven – jen průsečík
                } else if (aInside) {
                    Point2D intersection = polygonEdge.getIntersection(clipperEdge, false);
                    if (intersection != null)
                        newPoints.add(intersection);
                // Přichází dovnitř – průsečík a potom konec hrany
                } else if (bInside) {
                    Point2D intersection = polygonEdge.getIntersection(clipperEdge, false);
                    if (intersection != null)
                        newPoints.add(intersection);
                    newPoints.add(polygonEdgePointB);
                }
            }

            points = newPoints;
        }
    }

    public List<Edge> getEdges(Polygon polygon) {
        List<Edge> polygonEdges = new ArrayList<>();

        // rozlozeni na jednotlive hrany
        for(int i = 0; i < polygon.getSize(); i++) {
            int indexB = i + 1;
            if(indexB == polygon.getSize())
                indexB = 0;

            Point2D a = polygon.getPoint(i);
            Point2D b = polygon.getPoint(indexB);

            polygonEdges.add(new Edge(a.getX(), a.getY(), b.getX(), b.getY()));
        }

        return polygonEdges;
    }
}
