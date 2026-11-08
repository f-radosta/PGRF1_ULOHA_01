package fill;

import model.Edge;
import model.Point2D;
import model.Polygon;
import raster.Raster;
import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScanLineFiller implements Filler{
    private Polygon polygon;
    private int fillColor;
    private final Raster raster;
    private final PolygonRasterizer polygonRasterizer;

    public ScanLineFiller(int fillColor, Raster raster, PolygonRasterizer polygonRasterizer) {
        this.fillColor = fillColor;
        this.raster = raster;
        this.polygonRasterizer = polygonRasterizer;
    }

    @Override
    public void fill() {

        if (polygon == null || polygon.getSize() < 3)
            return;

        List<Edge> edges = new ArrayList<>();

        // rozlozeni na jednotlive hrany
        for(int i = 0; i < polygon.getSize(); i++) {
            int indexB = i + 1;
            if(indexB == polygon.getSize())
                indexB = 0;

            Point2D a = polygon.getPoint(i);
            Point2D b = polygon.getPoint(indexB);

            Edge edge = new Edge(a.getX(), a.getY(), b.getX(), b.getY());
            // vodorovne useky nechceme
            if(!edge.isHorizontal()) {
                // hrany orientujeme smerem dolu
                edge.orientate();
                edges.add(edge);
            }
        }

        // najít min a max Y
        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;
        for (int i = 0; i < polygon.getSize(); i++) {
            int pointY = polygon.getPoint(i).getY();
            if (pointY < yMin)
                yMin = pointY;
            if (pointY > yMax)
                yMax = pointY;
        }

        for (int y = yMin; y <= yMax; y++) {
            List<Integer> intersections = new ArrayList<>();

            for(Edge edge : edges) {
                // pokud existuje průsečík
                if (edge.isIntersection(y)) {
                    // spočítám ho a přidám do seznamu průsečíků
                    intersections.add(edge.getIntersectionX(y));
                }

            }

            // seřadit průsečíky od min po max
            Collections.sort(intersections);

            // obarvit
            for (int i = 0; i < intersections.size() - 1; i = i + 2) {
                for (int j = intersections.get(i); j < intersections.get(i + 1); j++) {
                    raster.setPixel(j, y, fillColor);
                }
            }
        }

        // obtáhnout
        polygonRasterizer.rasterize(polygon);
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
