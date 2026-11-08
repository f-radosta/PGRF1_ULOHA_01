package controller;

import fill.ScanLineFiller;
import fill.SeedFiller;
import model.Edge;
import model.Point2D;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
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
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    // Fillers
    private SeedFiller seedFiller;
    private ScanLineFiller scanLineFiller;

    private Polygon polygon;
    private Edge clipLine = new Edge(400, 400, 600, 100);
    private Edge normal;

    private Integer startX = null;
    private Integer startY = null;
    private Integer endX = null;
    private Integer endY = null;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        seedFiller = new SeedFiller(panel.getRaster());
        scanLineFiller = new ScanLineFiller(0x00ff00, panel.getRaster(), polygonRasterizer);
        polygon = new Polygon();

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
//                if(e.getButton() == MouseEvent.BUTTON2) {                     // clipLine;
//                    Point2D p = new Point2D(e.getX(), e.getY());
//
//                    // Spočítám tečný vektor
//                    Point2D a = new Point2D(clipLine.getX1(), clipLine.getY1());
//                    Point2D b = new Point2D(clipLine.getX2(), clipLine.getY2());
//
//                    // Tečný vektor
//                    int tx = b.getX() - a.getX();
//                    int ty = b.getY() - a.getY();
//                    // Normála - normálový vektor
//                    int nx = -ty;
//                    int ny = tx;
//                    // Vektor v - k bodu P
//                    int cx = p.getX() - a.getX();
//                    int cy = p.getY() - a.getY();
//                    // Skalární součin
//                    float dot = nx * cx + ny * cy;
//
//                    if(dot > 0) System.out.println("Vpravo");
//                    else  System.out.println("Vlevo");
//
//                    normal = new Edge(a.getX(), a.getY(), a.getX() + nx, a.getY() + ny);
//                }

                drawScene();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    polygon.addPoint(new Point2D(e.getX(), e.getY()));

                    startX = e.getX();
                    startY = e.getY();
                    endX = null;
                    endY = null;
                }

                drawScene();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();

                drawScene();
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();

        scanLineFiller.fill();

        if (startX != null && endX != null) {
            lineRasterizer.rasterize(startX, startY, endX, endY);
            lineRasterizer.rasterize(polygon.getPoint(0).getX(), polygon.getPoint(0).getY(), endX, endY);
        }

        polygonRasterizer.rasterize(polygon);
        //seedFiller.fill();

//        lineRasterizer.rasterize(clipLine.getX1(), clipLine.getY1(), clipLine.getX2(), clipLine.getY2());
//        if(normal != null)
//            lineRasterizer.rasterize(normal.getX1(), normal.getY1(), normal.getX2(), normal.getY2());

        panel.repaint();
    }
}
