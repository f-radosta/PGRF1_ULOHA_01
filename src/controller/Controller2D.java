package controller;

import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import view.Panel;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller2D {
    private final Panel panel;
    private Polygon polygon;
    private int nearestIndex = -1;

    private LineRasterizer lineRasterizer;

    public Controller2D(Panel panel) {
        this.panel = panel;

        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());

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
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    nearestIndex = findNearest(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    nearestIndex = -1;
                    return;
                }
                addPoint(e);
                panel.getRaster().clear();
                paintPolygon(e);
                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (nearestIndex < 0) {
                        return;
                    }
                    Point point = polygon.getPoint(nearestIndex);
                    point.setX(e.getX());
                    point.setY(e.getY());
                    panel.getRaster().clear();
                    paintPolygon(e);
                    panel.repaint();
                    return;
                }

                if (polygon.size() == 0) {
                    return;
                }

                int endX = e.getX();
                int endY = e.getY();

                panel.getRaster().clear();

                if (polygon.size() == 1) {
                    Point startPoint = polygon.getPoint(0);
                    lineRasterizer.rasterize(startPoint.getX(), startPoint.getY(), endX, endY);
                } else {
                    int edge = nearestEdge(endX, endY);
                    Point a = polygon.getPoint(edge);
                    // pokud je edge posledni vrchol, tak zbytkem po deleni ziskame prvni vrchol
                    Point b = polygon.getPoint((edge + 1) % polygon.size());
                    lineRasterizer.rasterize(a.getX(), a.getY(), endX, endY);
                    lineRasterizer.rasterize(b.getX(), b.getY(), endX, endY);
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
                Point pointEnd = polygon.getPoint(i + 1);
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

    private int findNearest(int x, int y) {
        int index = -1;
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < polygon.size(); i++) {
            int dx = polygon.getPoint(i).getX() - x;
            int dy = polygon.getPoint(i).getY() - y;
            int dist = dx * dx + dy * dy;
            if (dist < best) {
                best = dist;
                index = i;
            }
        }
        return index;
    }

    private int nearestEdge(int x, int y) {
        int index = 0;
        double best = Double.MAX_VALUE;
        for (int i = 0; i < polygon.size(); i++) {
            Point a = polygon.getPoint(i);
            Point b = polygon.getPoint((i + 1) % polygon.size());
            double dist = distanceToEdge(x, y, a, b);
            if (dist < best) {
                best = dist;
                index = i;
            }
        }
        return index;
    }

    private double distanceToEdge(int x, int y, Point a, Point b) {
        // smer hrany A -> B
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double len2 = dx * dx + dy * dy;
        // t = 0 je bod A, t = 1 je bod B
        double t = 0;
        if (len2 != 0) {
            t = ((x - a.getX()) * dx + (y - a.getY()) * dy) / len2;
        }
        // mimo usecku bereme blizsi koncovy vrchol
        if (t < 0) {
            t = 0;
        }
        if (t > 1) {
            t = 1;
        }
        // nejblizsi bod na hrane
        double px = a.getX() + t * dx;
        double py = a.getY() + t * dy;
        double ex = x - px;
        double ey = y - py;
        // vzdalenost na druhou, na porovnani hran staci
        return ex * ex + ey * ey;
    }

    private void addPoint(MouseEvent e) {
        int startX = e.getX();
        int startY = e.getY();
        Point newPoint = new Point(startX, startY);
        if (polygon.size() >= 2) {
            int edge = nearestEdge(startX, startY);
            polygon.addPoint(edge + 1, newPoint);
        } else {
            polygon.addPoint(newPoint);
        }
        // System.out.println("new point: " + startX + ", " + startY);
    }
}
