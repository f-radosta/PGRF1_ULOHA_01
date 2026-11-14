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
    private Edge clipLine = new Edge(400, 400, 600, 100);
    private Edge normal;

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

        if (startPoint != null && endPoint != null) { // pokud je co vykreslovat
            if (shiftMode) {
                Edge edge = new Edge(startPoint, endPoint);
                Point2D shiftModeEndPoint = edge.getShiftModeLineEnd(); // shift mode konec usecky
                lr.rasterize(startPoint, shiftModeEndPoint);
                if (polygon.getSize() > 1) // jak bude vypadat nove polygon
                    lr.rasterize(polygon.getPoint(0), shiftModeEndPoint);
            } else {
                lr.rasterize(startPoint, endPoint);
                if (polygon.getSize() > 1) // jak bude vypadat nove polygon
                    lr.rasterize(polygon.getPoint(0), endPoint);
            }
        }

        pr.rasterize(polygon);
        //seedFiller.fill();

//        lr.rasterize(clipLine.getX1(), clipLine.getY1(), clipLine.getX2(), clipLine.getY2());
//        if(normal != null)
//            lr.rasterize(normal.getX1(), normal.getY1(), normal.getX2(), normal.getY2());

        panel.repaint();
    }
}
