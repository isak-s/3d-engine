package d.engine.game;

import java.awt.event.*;

import javax.swing.*;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.ScreenPlane;


public class Player {
    public ScreenPlane screenPlane;
    public KeyListener keyListener;
    public MouseMotionAdapter mouseMotionAdapter;
    public MouseAdapter mouseAdapter;


    private JPanel panel;

    private int lastX, lastY;

    public Player() {
        screenPlane = new ScreenPlane(new PositionVector3D(0, 0, 0),
                                      new PositionVector3D(0, 0, 1));
        keyListener = new GameKeyListener();
        mouseMotionAdapter = new GameMouseMotionAdapter();
        mouseAdapter = new GameMouseAdapter();


    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    private void rotatePlane(int dx, int dy) {
        double angleY = Math.toRadians(dx); // horizontal drag -> Y-axis rotation
        double angleX = Math.toRadians(dy); // vertical drag -> X-axis rotation
        screenPlane.rotateAroundEyePos(angleX, angleY, 0);
    }

    // Movement: Add extra collision logic
    public void moveForward() {
        screenPlane.moveForward(10);
    }

    public void moveBackward() {
        screenPlane.moveBackward(10);
    }

    public void moveRight() {
        screenPlane.moveRight(10);
    }

    public void moveLeft() {
        screenPlane.moveLeft(10);
    }

    public void moveUp() {
        screenPlane.moveUp(10);
    }

    public void moveDown() {
        screenPlane.moveDown(10);
    }

    ///////////////////////////////////////


    public class GameKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_W: moveForward(); break;
                case KeyEvent.VK_A: moveLeft(); break;
                case KeyEvent.VK_D: moveRight(); break;
                case KeyEvent.VK_S: moveBackward(); break;

                case KeyEvent.VK_RIGHT: rotatePlane(10, 0); break;
                case KeyEvent.VK_LEFT: rotatePlane(-10, 0); break;

                default:
                    break;
            }
            panel.repaint();
        }

    }

    public class GameMouseMotionAdapter extends MouseMotionAdapter {
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

    public class GameMouseAdapter extends MouseAdapter {
        @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
    }
}
