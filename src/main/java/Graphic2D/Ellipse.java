/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphic2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class Ellipse extends JFrame {

    DrawingCanvas canvas;

    JLabel location;

    public Ellipse() {
        super();
        Container container = getContentPane();
        canvas = new DrawingCanvas();
        container.add(canvas);     
        location = new JLabel("");
        setSize(600, 300);
        setVisible(true);
    }

    class DrawingCanvas extends JPanel {

        double x, y, w, h;
        double x3, y3, w3, h3;
        int x1, y1, x2, y2;

        Ellipse2D ellipse;
        Rectangle2D rec;

        Ellipse2D selectedShape;
        Rectangle2D boundingRec;

        Cursor curCursor;

        public DrawingCanvas() {
            x = 30;
            y = 20;
            w = 100;
            h = 75;
            x3 = 150;
            y3 = 20;
            w3 = 100;
            h3 = 75;
            setBackground(Color.white);
            addMouseListener(new MyMouseListener());
            addMouseMotionListener(new MyMouseMotionListener());
        }

        public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            ellipse = new Ellipse2D.Double(x, y, w, h);
            g2D.draw(ellipse);

            if (boundingRec != null) {
                drawHighlightSquares(g2D, boundingRec);
            }
            if (curCursor != null) {
                setCursor(curCursor);
            }
        }

        public void drawHighlightSquares(Graphics2D g2D, Rectangle2D r) {
            double x = r.getX();
            double y = r.getY();
            double w = r.getWidth();
            double h = r.getHeight();
            g2D.setColor(Color.BLUE);

        }

        class MyMouseListener extends MouseAdapter {

            public void mousePressed(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    selectedShape = ellipse;
                    if (boundingRec != null) {
                        boundingRec = ellipse.getBounds2D();
                    }
                    displayParameters(selectedShape);
                } else {
                    boundingRec = null;
                    location.setText("");
                }
                canvas.repaint();
                x1 = e.getX();
                y1 = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    boundingRec = ellipse.getBounds2D();
                    selectedShape = ellipse;

                    displayParameters(selectedShape);
                }

                canvas.repaint();
            }

            public void mouseClicked(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    selectedShape = ellipse;
                    boundingRec = ellipse.getBounds2D();

                    displayParameters(selectedShape);
                } else {
                    if (boundingRec != null) {
                        boundingRec = null;
                    }
                    location.setText("");
                }
                canvas.repaint();
            }
        }

        class MyMouseMotionListener extends MouseMotionAdapter {

            public void mouseDragged(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    boundingRec = null;
                    selectedShape = ellipse;
                    x2 = e.getX();
                    y2 = e.getY();
                    x = x + x2 - x1;
                    y = y + y2 - y1;
                    x1 = x2;
                    y1 = y2;
                }
                if (selectedShape != null) {
                    displayParameters(selectedShape);
                }
                canvas.repaint();
            }

            public void mouseMoved(MouseEvent e) {
                if (ellipse != null) {
                    if (ellipse.contains(e.getX(), e.getY())) {
                        curCursor = Cursor
                                .getPredefinedCursor(Cursor.HAND_CURSOR);
                    } else {
                        curCursor = Cursor.getDefaultCursor();
                    }
                }
                canvas.repaint();
            }
        }

        public void displayParameters(Shape shape) {
            double x = selectedShape.getX();
            double y = selectedShape.getY();
            double w = selectedShape.getWidth();
            double h = selectedShape.getHeight();
            String locString = "(" + Double.toString(x) + ","
                    + Double.toString(y) + ")";
            String sizeString = "(" + Double.toString(w) + ","
                    + Double.toString(h) + ")";
            location.setText(locString);
        }
    }

}
