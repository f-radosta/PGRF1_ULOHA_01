package controller;

import fill.ScanLineFiller;
import fill.SeedFiller;
import model.Edge;
import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.PolygonRasterizer;
import view.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller2D {
    private final Panel panel;

    // Rasterizers
    private LineRasterizer lr;
    private PolygonRasterizer pr;

    // Fillers
    private SeedFiller seedFiller;
    private ScanLineFiller scanLineFiller;

    private Polygon polygon;
    Polygon clipper = new Polygon();
    Polygon clipperRectangle = new Polygon();

    private Point2D startPoint = null;
    private Point2D endPoint = null;

    private boolean shiftMode = false;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lr = new LineRasterizerTrivial(panel.getRaster());
        pr = new PolygonRasterizer(lr);
        seedFiller = new SeedFiller(panel.getRaster());
        scanLineFiller = new ScanLineFiller(0x00ff00, panel.getRaster(), pr);
        polygon = new Polygon();
        clipper.addPoint(new Point2D(100, 150));
        clipper.addPoint(new Point2D(400, 100));
        clipper.addPoint(new Point2D(500, 350));
        clipper.addPoint(new Point2D(300, 450));
        clipper.addPoint(new Point2D(100, 300));

        clipperRectangle.addPoint(new Point2D(200, 200));
        clipperRectangle.addPoint(new Point2D(350, 200));
        clipperRectangle.addPoint(new Point2D(350, 300));
        clipperRectangle.addPoint(new Point2D(200, 300));

        initListeners();
        drawScene();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    //seedFiller.setSeed(new Point2D(e.getX(), e.getY()));
                    scanLineFiller.setPolygon(polygon);
                    drawScene();
                }
                if(e.getButton() == MouseEvent.BUTTON2) {
                    polygon.clip(clipper);
                    if (polygon.getSize() > 1) {
                        startPoint = polygon.getPoint(0);
                        endPoint = polygon.getPoint(polygon.getSize()-1);
                    }
                }

                drawScene();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    Point2D newPoint = new Point2D(e.getX(), e.getY());
                    if (shiftMode)
                        newPoint = new Edge(startPoint, endPoint).getShiftModeLineEnd();
                    polygon.addPoint(newPoint);
                    startPoint = newPoint;
                    endPoint = null;
                }

                drawScene();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = new Point2D(e.getX(), e.getY());
                drawScene();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftMode =  true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftMode =  false;
                }
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();
        scanLineFiller.fill();

        drawDraggedLine();
        pr.rasterize(polygon);
        pr.rasterize(clipper);
        pr.rasterize(clipperRectangle);

        panel.repaint();
    }

    private void drawDraggedLine() {
        if (startPoint == null || endPoint == null)
            return;

        Point2D lineEnd = endPoint;
        if (shiftMode) {
            lineEnd = new Edge(startPoint, endPoint).getShiftModeLineEnd();
            if (polygon.getSize() > 1)
                lr.rasterize(polygon.getPoint(0), lineEnd);
        } else if (polygon.getSize() > 1) {
            lr.rasterize(polygon.getPoint(0), endPoint);
            lr.rasterize(polygon.getPoint(polygon.getSize() - 1), endPoint);
        }

        drawClippedLine(startPoint, lineEnd);
    }

    private void drawClippedLine(Point2D a, Point2D b) {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (int i = 0; i < clipperRectangle.getSize(); i++) {
            Point2D p = clipperRectangle.getPoint(i);
            xMin = Math.min(xMin, p.getX());
            yMin = Math.min(yMin, p.getY());
            xMax = Math.max(xMax, p.getX());
            yMax = Math.max(yMax, p.getY());
        }

        Edge clipped = new Edge(a, b).clipCohenSutherland(xMin, yMin, xMax, yMax);
        if (clipped != null)
            lr.rasterize(clipped.getX1(), clipped.getY1(), clipped.getX2(), clipped.getY2());
    }
}
