package rasterize;

import raster.Raster;

public class LineRasterizerBresenham extends LineRasterizer {

    public LineRasterizerBresenham(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int x = x1;
        int y = y1;
        raster.setPixel(x, y, 0xff0000);

        if (dx >= dy) {
            int p = 2 * dy - dx;
            int d1 = 2 * dy;
            int d2 = 2 * (dy - dx);

            while (x != x2) {
                x = x + sx;
                if (p < 0) {
                    p = p + d1;
                } else {
                    p = p + d2;
                    y = y + sy;
                }
                raster.setPixel(x, y, 0xff0000);
            }
        } else {
            int p = 2 * dx - dy;
            int d1 = 2 * dx;
            int d2 = 2 * (dx - dy);

            while (y != y2) {
                y = y + sy;
                if (p < 0) {
                    p = p + d1;
                } else {
                    p = p + d2;
                    x = x + sx;
                }
                raster.setPixel(x, y, 0xff0000);
            }
        }
    }
}
