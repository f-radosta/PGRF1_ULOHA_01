package rasterize;

import raster.Raster;

public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {

        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

        if (Math.abs(x2 - x1) == 0) {
            for(int y = y1; y <= y2; y++) {
                raster.setPixel(x1, y, 0xff0000);
            }
            return;
        }

        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            if (x1 > x2) {
                int pom = x1;
                x1 = x2;
                x2 = pom;
            }

            for(int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, 0xff0000);
            }
        } else {
            if (y1 > y2) {
                int pom = y1;
                y1 = y2;
                y2 = pom;
            }

            for(int y = y1; y <= y2; y++) {
                int x = Math.round((y - q) / k);
                raster.setPixel(x, y, 0xff0000);
            }
        }

    }
}
