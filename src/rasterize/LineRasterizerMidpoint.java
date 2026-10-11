package rasterize;

import raster.Raster;

public class LineRasterizerMidpoint extends LineRasterizer {

    public LineRasterizerMidpoint(Raster raster) {
        super(raster);
    }

    // Midpoint
    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        raster.setPixel(x1, y1, 0xff0000);
        raster.setPixel(x2, y2, 0xff0000);
        midPoint(x1, y1, x2, y2);
    }

    private void midPoint(int x1, int y1, int x2, int y2) {
        int sx = (x1 + x2) / 2;
        int sy = (y1 + y2) / 2;
        raster.setPixel(sx, sy, 0xff0000);

        if ((Math.abs(x1 - sx) > 1) || (Math.abs(y1 - sy) > 1)) {
            midPoint(x1, y1, sx, sy);
        }
        if ((Math.abs(x2 - sx) > 1) || (Math.abs(y2 - sy) > 1)) {
            midPoint(sx, sy, x2, y2);
        }
    }
}
