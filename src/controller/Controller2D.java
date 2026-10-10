package controller;

import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerBresenham;
import rasterize.LineRasterizerDDA;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerMidpoint;
import rasterize.LineRasterizerTrivial;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller2D {
    private final Panel panel;
    private Polygon polygon;

    // Rasterizers
    private LineRasterizer lineRasterizer;

    public Controller2D(Panel panel) {
        this.panel = panel;

//        lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        //lineRasterizer = new LineRasterizerDDA(panel.getRaster());
        //lineRasterizer = new LineRasterizerMidpoint(panel.getRaster());
        //lineRasterizer = new LineRasterizerBresenham(panel.getRaster());

        polygon = new Polygon();

        initListeners();
    }

    private void initListeners() {

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'c') {
                    polygon.clear();
                    panel.getRaster().clear();
                    panel.repaint();
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseReleased(MouseEvent e) {
               addPoint(e);
               panel.getRaster().clear();
               paintPolygon(e);
               panel.repaint();
           }
       });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (polygon.size() == 0) {
                    return;
                }

                int endX = e.getX();
                int endY = e.getY();
                Point startPoint = polygon.getPoint(0);
                int startX = startPoint.getX();
                int startY = startPoint.getY();

                panel.getRaster().clear();

                lineRasterizer.rasterize(startX, startY, endX, endY);
                if (polygon.size() >= 2) {
                    lineRasterizer.rasterize(
                            polygon.getPoint(polygon.size() - 1).getX(),
                            polygon.getPoint(polygon.size() - 1).getY(),
                            endX,
                            endY
                    );
                }
                paintPolygon(e);

                panel.repaint();
            }
        });

    }

    private void paintPolygon(MouseEvent e) {
        if (polygon.size() >= 2) {
            for (int i = 0; i < polygon.size() - 1; i++) {
                Point pointStart = polygon.getPoint(i);
                Point pointEnd = polygon.getPoint(i+1);
                lineRasterizer.rasterize(pointStart.getX(), pointStart.getY(), pointEnd.getX(), pointEnd.getY());
            }
            lineRasterizer.rasterize(
                    polygon.getPoint(0).getX(),
                    polygon.getPoint(0).getY(),
                    polygon.getPoint(polygon.size() - 1).getX(),
                    polygon.getPoint(polygon.size() - 1).getY()
            );
        }
    }

    private void addPoint(MouseEvent e) {
        int startX = e.getX();
        int startY = e.getY();
        Point newPoint = new Point(startX, startY);
        polygon.addPoint(newPoint);
        panel.getRaster().setPixel(startX, startY, 0xff0000);
        System.out.println("new point: " + startX + ", " + startY);
    }
}
