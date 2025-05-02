package d.engine;

import d.engine.bodies.Ground;
import d.engine.bodies.Tetrahedron;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.ScreenPlane;
import d.engine.geometry.Shape3D;
import d.engine.gui.GameDemoWindow;
import d.engine.gui.ShapeDemoWindow;

public class GameDemo {

     public static void main(String[] args) {

        System.err.println("game");

        Shape3D ground = new Ground(10);

        System.out.println("Unique vertices: " + ground.vertices.size());

        // ground.setTransparrent();

        Shape3D[] shapes = {ground};

        ScreenPlane screenPlane = new ScreenPlane(new PositionVector3D(0, 0, 0),
                                                  new PositionVector3D(0, 0, 1));

        ground.applyScalar(100);

        ground.setPosition(new PositionVector3D(0, -100, 0));

        screenPlane.setOrigin(new PositionVector3D(0, 0, 0));

        new GameDemoWindow(shapes, screenPlane, "Game demo");

    }

}
