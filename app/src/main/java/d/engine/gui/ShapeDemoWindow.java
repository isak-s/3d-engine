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

public class ShapeDemoWindow {

    private String title;

    private ScreenPlane screenPlane;
    private Shape3D[] shapes;
    private int lastX, lastY;
    private JPanel panel;

    private String[] projectionModes = {"Raycasted projection", "Plane projection"};

    private String projectionMode = projectionModes[0];

    public ShapeDemoWindow(Shape3D[] shapes, ScreenPlane screenPlane, String title) {
        this.shapes = shapes;
        this.screenPlane = screenPlane;
        this.title = title;
        javax.swing.SwingUtilities.invokeLater(this::createWindow);
    }

    private void createWindow() {
        JFrame frame = new JFrame(title);
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
        JPanel sliderPanel = new JPanel(new GridLayout(3, 2));

        // Focal length slider
        JSlider FOVSlider = new JSlider(10, 110, screenPlane.getFovX());
        FOVSlider.setBorder(BorderFactory.createTitledBorder("Field of view: " + screenPlane.getFovX()));
        FOVSlider.addChangeListener(e -> {
            int fov = FOVSlider.getValue();
            screenPlane.setFovX(fov);

            TitledBorder border = (TitledBorder) FOVSlider.getBorder();
            border.setTitle("Field of view: " + screenPlane.getFovX());
            FOVSlider.repaint();

            panel.repaint();
        });

        // Distance slider
        int initialZ = (int)screenPlane.getOrigin().z;

        JSlider distanceSlider = new JSlider(-300, 300, initialZ);
        distanceSlider.setBorder(BorderFactory.createTitledBorder("Plane's Z-coordinate"));
        distanceSlider.addChangeListener(e -> {
            double z = distanceSlider.getValue();

            screenPlane.setOrigin(new PositionVector3D(0, 0, z));

            TitledBorder border = (TitledBorder) distanceSlider.getBorder();
            border.setTitle("Plane's Z-coordinate: " + z);
            distanceSlider.repaint();

            panel.repaint();
        });


        // Scalar
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

        // Projection mode
        ButtonGroup group = new ButtonGroup();

        JRadioButton rayCastedButton = new JRadioButton(projectionModes[0]);
        rayCastedButton.addActionListener(e -> {
            projectionMode = projectionModes[0];
            panel.repaint();
        });

        JRadioButton planeButton = new JRadioButton(projectionModes[1]);
        planeButton.addActionListener(e -> {
            projectionMode = projectionModes[1];
            panel.repaint();
        });
        group.add(rayCastedButton);
        sliderPanel.add(rayCastedButton);
        group.add(planeButton);
        sliderPanel.add(planeButton);

        rayCastedButton.setSelected(true);

        sliderPanel.add(FOVSlider);
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

        ArrayList<Triangle> triangles = new ArrayList<>();
        shape.forEach(triangles::add); // collect triangles

        // Sort by average Z depth (back to front)
        triangles.sort((t1, t2) -> {
            return Double.compare(t2.averageZ(), t1.averageZ()); // painter's algorithm (farther first)
        });

        triangles.forEach((Triangle t) -> {
            ScreenPlane.ScreenCoordinate p1;
            ScreenPlane.ScreenCoordinate p2;
            ScreenPlane.ScreenCoordinate p3;

            if (projectionMode == "Raycasted projection") {
                p1 = screenPlane.projectPointRayCasted(t.getA().add(pos));
                p2 = screenPlane.projectPointRayCasted(t.getB().add(pos));
                p3 = screenPlane.projectPointRayCasted(t.getC().add(pos));
            } else {
                p1 = screenPlane.screenCoordinate(screenPlane.projectPoint(t.getA().add(pos)));
                p2 = screenPlane.screenCoordinate(screenPlane.projectPoint(t.getB().add(pos)));
                p3 = screenPlane.screenCoordinate(screenPlane.projectPoint(t.getC().add(pos)));
            }

            if (p1 == null || p2 == null || p3 == null) {
                // System.err.println("null");
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

}
