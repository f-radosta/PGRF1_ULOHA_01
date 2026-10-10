package controller;

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

    // Rasterizers
    private LineRasterizer lineRasterizer;
    private int startX;
    private int startY;

    public Controller2D(Panel panel) {
        this.panel = panel;

//        lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        //lineRasterizer = new LineRasterizerDDA(panel.getRaster());
        //lineRasterizer = new LineRasterizerMidpoint(panel.getRaster());
        //lineRasterizer = new LineRasterizerBresenham(panel.getRaster());

        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int endX = e.getX();
                int endY = e.getY();

                panel.getRaster().clear();
                lineRasterizer.rasterize(startX, startY, endX, endY);
                panel.repaint();
            }
        });

    }
}
