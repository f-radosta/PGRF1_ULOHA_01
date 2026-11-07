package fill;

import model.Edge;
import model.Point2D;
import model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ScanLineFiller implements Filler{
    private Polygon polygon;
    private int fillColor;

    public ScanLineFiller(int fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void fill() {

        List<Edge> edges = new ArrayList<>();

        for(int i = 0; i < polygon.getSize(); i++) {
            int indexB = i + 1;
            if(indexB == polygon.getSize())
                indexB = 0;

            Point2D a = polygon.getPoint(i);
            Point2D b = polygon.getPoint(indexB);

            Edge edge = new Edge(a.getX(), a.getY(), b.getX(), b.getY());
            if(!edge.isHorizontal()) {
                edge.orientate();
                edges.add(edge);
            }
        }

        // TODO: najít min a max Y
        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;

        for (int y = yMin; y <= yMax; y++) {
            List<Integer> intersections = new ArrayList<>();

            for(Edge edge : edges) {
                // TODO: pokud existuje průsečík
                // TODO: spočítám ho a přidám do seznamu průsečíků
            }

            // TODO: seřadit průsečíky od min po max

            // TODO: obarvit
        }

        // TODO: obtáhnout
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
