package rasterize;

import raster.Raster;

import static java.lang.Math.*;

public class LineRasterizerDDA extends LineRasterizer {

    public LineRasterizerDDA(Raster raster) {
        super(raster);
    }

    // DDA
    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {

        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = max(abs(dx), abs(dy));
        float x_increment = dx / (float) steps;
        float y_increment = dy / (float) steps;
        float x = x1, y = y1;
        raster.setPixel(round(x), round(y), 0xff0000);
        for (int i = 0; i < steps; i++) {
            raster.setPixel(round(x), round(y), 0xff0000);
            x = x + x_increment;
            y = y + y_increment;
        }

    }
}
