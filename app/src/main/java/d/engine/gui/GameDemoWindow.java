package d.engine.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.*;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.ScreenPlane;
import d.engine.geometry.Shape3D;
import d.engine.geometry.Triangle;
import d.engine.util.Constants;


public class GameDemoWindow {

    private String title;

    private ScreenPlane screenPlane;
    private Shape3D[] shapes;
    private int lastX, lastY;
    private JPanel panel;

    public GameDemoWindow(Shape3D[] shapes, ScreenPlane screenPlane, String title) {
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
        panel = new GamePanel();

        // key and mouse handling
        panel.addMouseListener(new GameMouseAdapter());
        panel.addMouseMotionListener(new GameMouseMotionAdapter());
        panel.addKeyListener(new GameKeyListener());

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        frame.setVisible(true);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    private void rotatePlane(int dx, int dy) {
        double angleY = Math.toRadians(dx); // horizontal drag -> Y-axis rotation
        double angleX = Math.toRadians(dy); // vertical drag -> X-axis rotation

        screenPlane.rotateAroundPoint(screenPlane.eyePos, angleX, angleY, 0);
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

            ScreenPlane.ScreenCoordinate p1 = screenPlane.projectPointRayCasted(t.getA().add(pos));
            ScreenPlane.ScreenCoordinate p2 = screenPlane.projectPointRayCasted(t.getB().add(pos));
            ScreenPlane.ScreenCoordinate p3 = screenPlane.projectPointRayCasted(t.getC().add(pos));

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


    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.black);
            renderShapes(g);
        }
    }

    private class GameKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            System.err.println(key);
            if (key == KeyEvent.VK_LEFT) {
                rotatePlane(-10, 0);
                panel.repaint();
            }
            else if (key == KeyEvent.VK_RIGHT) {
                rotatePlane(10, 0);
                panel.repaint();
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

    }

    private class GameMouseMotionAdapter extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - lastX;
            int dy = e.getY() - lastY;

            rotatePlane(dx, dy);

            lastX = e.getX();
            lastY = e.getY();
            panel.repaint();
        }
    }

    private class GameMouseAdapter extends MouseAdapter {
        @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
    }
}
