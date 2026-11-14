package rasterize;

import model.Point2D;
import raster.Raster;

public abstract class LineRasterizer {
    protected Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2) {
        System.out.println("===== rasterize předek =====");
    }

    public void rasterize(Point2D startPoint, Point2D endPoint) {}
}
