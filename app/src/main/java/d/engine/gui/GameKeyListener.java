package d.engine.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {

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
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

}