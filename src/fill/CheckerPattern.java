package fill;

public class CheckerPattern implements PatternFill {
    private final int cellSize;
    private final int colorA;
    private final int colorB;

    public CheckerPattern() {
        this(8, 0x999999, 0x222222);
    }

    public CheckerPattern(int cellSize, int colorA, int colorB) {
        this.cellSize = cellSize;
        this.colorA = colorA;
        this.colorB = colorB;
    }

    @Override
    public int paint(int x, int y) {
        int cellX = Math.floorDiv(x, cellSize);
        int cellY = Math.floorDiv(y, cellSize);
        if ((cellX + cellY) % 2 == 0)
            return colorA;
        return colorB;
    }
}
