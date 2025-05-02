package d.engine;

import d.engine.bodies.D20;
import d.engine.bodies.Tetrahedron;
import d.engine.geometry.PositionVector3D;
import d.engine.geometry.ScreenPlane;
import d.engine.geometry.Shape3D;
import d.engine.gui.ShapeDemoWindow;

public class SingleShapeDemo {

    public static void main(String[] args) {

        Shape3D shape = new Tetrahedron();

        // shape.setTransparrent();

        Shape3D[] shapes = {shape};

        ScreenPlane screenPlane = new ScreenPlane(new PositionVector3D(0, 0, 0),
                                                  new PositionVector3D(0, 0, 1));

        shape.applyScalar(100);

        shape.setPosition(new PositionVector3D(0, 0, 200));

        new ShapeDemoWindow(shapes, screenPlane, "Single shape demo");

        System.out.println("Unique vertices: " + shape.vertices.size());

    }
}
