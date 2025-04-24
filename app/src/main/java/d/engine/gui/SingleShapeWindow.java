package d.engine.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

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
    private JPanel panel;

    public SingleShapeWindow(Shape3D shape, ScreenPlane screenPlane) {
        this.shape = shape;
        this.screenPlane = screenPlane;
        javax.swing.SwingUtilities.invokeLater(this::createWindow);
    }

    private void createWindow() {
        JFrame frame = new JFrame("ShapeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Rendering panel
        panel = new JPanel() {
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


                // rotateShape(dx, dy);


                rotatePlane(dx, dy);

                lastX = e.getX();
                lastY = e.getY();
                panel.repaint();
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(createSliders(), BorderLayout.SOUTH);

        frame.setSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT + 100));
        frame.setVisible(true);
    }

    private JPanel createSliders() {
        JPanel sliderPanel = new JPanel(new GridLayout(3, 1));

        // Focal length slider
        JSlider focalSlider = new JSlider(1, 100, (int)(Constants.focalLength * 100));
        focalSlider.setBorder(BorderFactory.createTitledBorder("Focal Length (x100)"));
        focalSlider.addChangeListener(e -> {
            Constants.focalLength = focalSlider.getValue() / 1000.0;
            screenPlane = new ScreenPlane(new PositionVector3D(0, 0, 0), new PositionVector3D(0, 0, 1));

            TitledBorder border = (TitledBorder) focalSlider.getBorder();
            border.setTitle("Focal Length: " + Constants.focalLength);
            focalSlider.repaint();

            panel.repaint();
        });

        // Distance slider
        int initialZ = (int)(shape.getPosition().z);
        initialZ = Math.max(1, Math.min(300, initialZ));

        JSlider distanceSlider = new JSlider(1, 300, initialZ);
        distanceSlider.setBorder(BorderFactory.createTitledBorder("Distance to Object (x100)"));
        distanceSlider.addChangeListener(e -> {
            double z = distanceSlider.getValue();
            shape.setPosition(new PositionVector3D(0, 0, z));

            TitledBorder border = (TitledBorder) distanceSlider.getBorder();
            border.setTitle("Distance to object: " + z);
            distanceSlider.repaint();

            panel.repaint();
        });

        // Scalar slider
        JSlider scaleSlider = new JSlider(1, 5000, 1);
        scaleSlider.setBorder(BorderFactory.createTitledBorder("Scale"));
        scaleSlider.addChangeListener(e -> {
            double scale = scaleSlider.getValue();
            shape.resetTransformations();
            shape.applyScalar(scale);

            TitledBorder border = (TitledBorder) scaleSlider.getBorder();
            border.setTitle("Scale: " + scale);
            scaleSlider.repaint();

            panel.repaint();
        });

        sliderPanel.add(focalSlider);
        sliderPanel.add(distanceSlider);
        sliderPanel.add(scaleSlider);
        return sliderPanel;
    }

    private void renderShape(Graphics g) {

        System.err.println(shape.computeCentroid());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);

        PositionVector3D pos = shape.getPosition();

        shape.forEach((Triangle t) -> {
            ScreenPlane.ScreenCoordinate p1 = screenPlane.ScreenCoordinateFromPositionVector(screenPlane.projectPoint(t.getA().add(pos)));
            ScreenPlane.ScreenCoordinate p2 = screenPlane.ScreenCoordinateFromPositionVector(screenPlane.projectPoint(t.getB().add(pos)));
            ScreenPlane.ScreenCoordinate p3 = screenPlane.ScreenCoordinateFromPositionVector(screenPlane.projectPoint(t.getC().add(pos)));

            if (p1 == null || p2 == null || p3 == null) {
                System.err.println("null");
                return;
            }

            g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            g2d.drawLine(p2.getX(), p2.getY(), p3.getX(), p3.getY());
            g2d.drawLine(p3.getX(), p3.getY(), p1.getX(), p1.getY());
        });
    }

    private void rotateShape(int dx, int dy) {
        double angleY = Math.toRadians(dx);
        double angleX = Math.toRadians(dy);
        shape.rotateAroundCentroid(angleX, angleY, 0);
    }

    private void rotatePlane(int dx, int dy) {
        double angleY = Math.toRadians(dx); // horizontal drag -> Y-axis rotation
        double angleX = Math.toRadians(dy); // vertical drag -> X-axis rotation

        screenPlane.rotateAroundPoint(new Point3D(0, 0, 0), angleX, angleY, 0); // You'll need to implement this
    }

}
