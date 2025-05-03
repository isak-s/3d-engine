package d.engine;

import d.engine.bodies.Ground;
import d.engine.game.Player;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.Shape3D;
import d.engine.gui.GameDemoWindow;

public class GameDemo {

     public static void main(String[] args) {

        Shape3D ground = new Ground(10);

        System.out.println("Unique vertices: " + ground.vertices.size());

        // ground.setTransparrent();

        Shape3D[] shapes = {ground};

        Player player = new Player();

        ground.applyScalar(100);

        ground.setPosition(new PositionVector3D(0, -100, 0));

        player.screenPlane.setOrigin(new PositionVector3D(0, 0, 0));

        new GameDemoWindow(shapes, player, "Game demo");

    }

}
