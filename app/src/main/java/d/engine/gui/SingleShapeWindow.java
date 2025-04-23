package d.engine.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

import d.engine.geomety.Point3D;
import d.engine.geomety.PositionVector3D;
import d.engine.geomety.ScreenPlane;
import d.engine.geomety.Shape3D;
import d.engine.geomety.Triangle;
import d.engine.util.Constants;

public class SingleShapeWindow {

    private ScreenPlane screenPlane;

    private Shape3D shape;

    private int lastX, lastY;

    public SingleShapeWindow(Shape3D shape, ScreenPlane screenPlane) {
        this.shape = shape;
        this.screenPlane = screenPlane;
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

        PositionVector3D pos = shape.getPosition();

        shape.forEach((Triangle t) -> {

            ScreenPlane.ScreenCoordinate p1 = screenPlane.projectPointRayCasted(t.getA().add(pos));
            ScreenPlane.ScreenCoordinate p2 = screenPlane.projectPointRayCasted(t.getB().add(pos));
            ScreenPlane.ScreenCoordinate p3 = screenPlane.projectPointRayCasted(t.getC().add(pos));

            if (p1 == null || p2 == null || p3 == null) {
                return; // Skip this triangle
            }

            System.err.println("Original:" + t.getA() + " " + t.getB() + " " + t.getC());
            System.err.println("projected:" + p1.getX() + " " + p1.getY());

            g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            g2d.drawLine(p2.getX(), p2.getY(), p3.getX(), p3.getY());
            g2d.drawLine(p3.getX(), p3.getY(), p1.getX(), p1.getY());
            }
        );


    }

private void rotateShape(int dx, int dy) {
    double angleY = Math.toRadians(dx);
    double angleX = Math.toRadians(dy);

    shape.rotateAroundCentroid(angleX, angleY, 0);
}
}
