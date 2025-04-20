package d.engine.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

import d.engine.geomety.Point3D;
import d.engine.geomety.Shape3D;
import d.engine.geomety.Triangle;
import d.engine.util.Constants;

public class SingleShapeWindow {

    private Shape3D shape;

    private int lastX, lastY;

    public SingleShapeWindow(Shape3D shape) {
        this.shape = shape;
        javax.swing.SwingUtilities.invokeLater(this::createWindow);
    }
    private void createWindow() {
        JFrame frame = new JFrame("ShapeDemo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(Color.black);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.black);
                renderShape(g);
            }
        };


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - lastX;
                int dy = e.getY() - lastY;

                rotateShape(dx, dy); // Rotate based on mouse movement

                lastX = e.getX();
                lastY = e.getY();

                panel.repaint();
            }
        });

        frame.add(panel);

        frame.setSize(new Dimension(Constants.SCREEN_HEIGHT, Constants.SCREEN_WIDTH));

        frame.setVisible(true);
    }

    private void renderShape(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);

        shape.forEach((Triangle t) -> {

            Point p1 = t.getA().projectOntoScreenPlane();
            Point p2 = t.getB().projectOntoScreenPlane();
            Point p3 = t.getC().projectOntoScreenPlane();

            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            g2d.drawLine(p2.x, p2.y, p3.x, p3.y);
            g2d.drawLine(p3.x, p3.y, p1.x, p1.y);
            }
        );


    }

private void rotateShape(int dx, int dy) {
    double angleY = Math.toRadians(dx);
    double angleX = Math.toRadians(dy);

    shape.rotateAroundCentroid(angleX, angleY);
}
}
