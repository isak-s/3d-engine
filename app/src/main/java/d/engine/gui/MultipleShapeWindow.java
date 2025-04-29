package d.engine.gui;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import d.engine.geometry.Point3D;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.ScreenPlane;
import d.engine.geometry.Shape3D;
import d.engine.geometry.Triangle;
import d.engine.util.Constants;

public class MultipleShapeWindow {

    private ScreenPlane screenPlane;
    private Shape3D[] shapes;
    private int lastX, lastY;
    private JPanel panel;

    public MultipleShapeWindow(Shape3D[] shapes, ScreenPlane screenPlane) {
        this.shapes = shapes;
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
                renderShapes(g);
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


                rotateShapes(dx, dy);


                // rotatePlane(dx, dy);

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
        focalSlider.setBorder(BorderFactory.createTitledBorder("Focal Length in centimeters"));
        focalSlider.addChangeListener(e -> {
            Constants.focalLength = focalSlider.getValue() / 1000.0;
            screenPlane = new ScreenPlane(new PositionVector3D(0, 0, 0), new PositionVector3D(0, 0, 1));

            TitledBorder border = (TitledBorder) focalSlider.getBorder();
            border.setTitle("Focal Length in centimeters: " + Constants.focalLength);
            focalSlider.repaint();

            panel.repaint();
        });

        // Distance slider
        int initialZ = (int)(shapes[0].getPosition().z);
        initialZ = Math.max(1, Math.min(300, initialZ));

        JSlider distanceSlider = new JSlider(1, 300, initialZ);
        distanceSlider.setBorder(BorderFactory.createTitledBorder("Distance to Object"));
        distanceSlider.addChangeListener(e -> {
            double z = distanceSlider.getValue();

            // TODO: Different distances for all shapes
            Stream.of(shapes).forEach(shape -> shape.setPosition(new PositionVector3D(0, 0, z)));

            TitledBorder border = (TitledBorder) distanceSlider.getBorder();
            border.setTitle("Distance to object: " + z);
            distanceSlider.repaint();

            panel.repaint();
        });

        // Scalar slider
        JButton x2Button = new JButton("1.1x scalar");
        x2Button.addActionListener(e -> {
            Stream.of(shapes).forEach(s -> s.applyScalar(1.1));
            panel.repaint();
        });
        JButton x05Button = new JButton("0.9x scalar");
        x05Button.addActionListener(e -> {
            Stream.of(shapes).forEach(s -> s.applyScalar(0.9));
            panel.repaint();
        });

        sliderPanel.add(focalSlider);
        sliderPanel.add(distanceSlider);
        sliderPanel.add(x05Button);
        sliderPanel.add(x2Button);
        return sliderPanel;
    }

    private void renderShapes(Graphics g) {
        Stream.of(shapes).forEach(s -> renderShape(g, s));

    }

    private void renderShape(Graphics g, Shape3D shape) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);

        PositionVector3D pos = shape.getPosition();

        System.err.println("Screen position: " + screenPlane.getPos());
        ArrayList<Triangle> triangles = new ArrayList<>();
        shape.forEach(triangles::add); // collect triangles

        // Sort by average Z depth (back to front)
        triangles.sort((t1, t2) -> {
            return Double.compare(t2.averageZ(), t1.averageZ()); // painter's algorithm (farther first)
        });

        triangles.forEach((Triangle t) -> {
            ScreenPlane.ScreenCoordinate p1 = screenPlane.ScreenCoordinateFromPositionVector(screenPlane.projectPoint(t.getA().add(pos)));
            ScreenPlane.ScreenCoordinate p2 = screenPlane.ScreenCoordinateFromPositionVector(screenPlane.projectPoint(t.getB().add(pos)));
            ScreenPlane.ScreenCoordinate p3 = screenPlane.ScreenCoordinateFromPositionVector(screenPlane.projectPoint(t.getC().add(pos)));

            if (p1 == null || p2 == null || p3 == null) {
                System.err.println("null");
                return;
            }

            g2d.setColor(t.color);

            if (t.isTransparent) {
                g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                g2d.drawLine(p2.getX(), p2.getY(), p3.getX(), p3.getY());
                g2d.drawLine(p3.getX(), p3.getY(), p1.getX(), p1.getY());
            }
            else {

                int[] xs = {p1.getX(), p2.getX(), p3.getX()};
                int[] ys = {p1.getY(), p2.getY(), p3.getY()};

                g2d.fillPolygon(xs, ys, 3);

            }
        });
    }

    private void rotateShapes(int dx, int dy) {
        double angleY = Math.toRadians(dx);
        double angleX = Math.toRadians(dy);
        Stream.of(shapes).forEach(s -> s.rotateAroundCentroid(angleX, angleY, 0));
    }

    private void rotateShape(int dx, int dy, Shape3D shape) {
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
