package fill;

import model.Point2D;
import raster.Raster;

public class SeedFiller implements Filler{
    private final Raster raster;
    private int fillColor;
    private int backgroundColor;
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
        // 1. Načtu barvu z rasteru na souřadnicích seedu
        int pixelColor = raster.getPixel(x, y);
        // TODO: 2. Porovnám načtenou barvu s barvou pozadí
        // TODO: 3. Pokud se nerovnají, nedělám nic
        // TODO: 4. Pokud se rovnají tak obravím
        // TODO: to samé udělám pro sousední pixely
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setSeed(Point2D seed) {
        this.seed = seed;

        int seedColor =  raster.getPixel(seed.getX(), seed.getY());
        setBackgroundColor(seedColor);
    }
}
