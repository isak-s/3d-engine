package d.engine.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import d.engine.geomety.Shape3D;

public class SingleShapeWindow {

    private Shape3D shape;

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
                renderShape(g); // Custom method to render Shape3D
            }
        };

        frame.add(panel);

        frame.setSize(new Dimension(500, 500));

        frame.setVisible(true);
    }

    private void renderShape(Graphics g) {

        // sort all shapes into a linked list with ascending order

        // Project shapes onto the screen plane in that order

        // if a shape is projected onto the projection of another projection,
            // reconstruct the new shape to only show until the intersection.

        // draw all projections

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
    }
}
