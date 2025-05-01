package d.engine;

import java.lang.reflect.Array;

import d.engine.bodies.D20;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.ScreenPlane;
import d.engine.geometry.Shape3D;
import d.engine.gui.ShapeDemoWindow;

public class SingleShapeDemo {

    public static void main(String[] args) {

        Shape3D d20 = new D20();

        d20.setTransparrent();

        Shape3D[] shapes = {d20};

        ScreenPlane screenPlane = new ScreenPlane(new PositionVector3D(0, 0, -300),
                                                  new PositionVector3D(0, 0, 1));

        d20.applyScalar(20);

        // d20.setPosition(new PositionVector3D(0, 0, -90));

        new ShapeDemoWindow(shapes, screenPlane, "Single shape demo");

        System.out.println("Unique vertices: " + d20.vertices.size());

    }
}
