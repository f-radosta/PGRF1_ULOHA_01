package fill;

import model.Point2D;
import raster.Raster;

public class SeedFiller implements Filler{
    private static final int BOUNDARY_COLOR = 0x00ff0000;

    private final Raster raster;
    private int fillColor;
    private int backgroundColor;
    private PatternFill pattern;
    private Point2D seed;

    public SeedFiller(Raster raster) {
        this.raster = raster;
        this.fillColor = 0x00ff00;
    }

    @Override
    public void fill() {
        if(seed == null) return;

        seedFill(seed.getX(), seed.getY());
    }

    private void seedFill(int x, int y) {
        Integer pixelColor = raster.getPixel(x, y);
        if (pixelColor == null) {
            return;
        }
        // červená hranice útvaru
        if ((pixelColor & 0x00ffffff) == BOUNDARY_COLOR) {
            return;
        }
        // jen pixely původního pozadí
        if (pixelColor == backgroundColor) {
            raster.setPixel(x, y, colorAt(x, y));
            seedFill(x + 1, y);
            seedFill(x - 1, y);
            seedFill(x, y + 1);
            seedFill(x, y - 1);
        }
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setPattern(PatternFill pattern) {
        this.pattern = pattern;
    }

    private int colorAt(int x, int y) {
        if (pattern != null)
            return pattern.paint(x, y);
        return fillColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setSeed(Point2D seed) {
        this.seed = seed;
        if (seed == null)
            return;

        int seedColor =  raster.getPixel(seed.getX(), seed.getY());
        setBackgroundColor(seedColor);
    }
}
