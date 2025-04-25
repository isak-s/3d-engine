package d.engine.bodies;

import d.engine.geomety.Point3D;
import d.engine.geomety.Shape3D;
import d.engine.geomety.Triangle;

public class Cube extends Shape3D {
    public Cube() {
        super(createCubeFaces());
    }

    private static Triangle[] createCubeFaces() {
    // 8 cube vertices (±0.5 in each axis to center it at origin)
    Point3D A = new Point3D(-0.5, -0.5, -0.5);
    Point3D B = new Point3D( 0.5, -0.5, -0.5);
    Point3D C = new Point3D( 0.5,  0.5, -0.5);
    Point3D D = new Point3D(-0.5,  0.5, -0.5);
    Point3D E = new Point3D(-0.5, -0.5,  0.5);
    Point3D F = new Point3D( 0.5, -0.5,  0.5);
    Point3D G = new Point3D( 0.5,  0.5,  0.5);
    Point3D H = new Point3D(-0.5,  0.5,  0.5);

    return new Triangle[] {
        // Bottom face (A, B, C, D)
        new Triangle(A, B, C),
        new Triangle(A, C, D),

        // Top face (E, F, G, H)
        new Triangle(E, G, F),
        new Triangle(E, H, G),

        // Front face (A, B, F, E)
        new Triangle(A, F, B),
        new Triangle(A, E, F),

        // Back face (D, C, G, H)
        new Triangle(D, C, G),
        new Triangle(D, G, H),

        // Left face (A, D, H, E)
        new Triangle(A, D, H),
        new Triangle(A, H, E),

        // Right face (B, C, G, F)
        new Triangle(B, G, C),
        new Triangle(B, F, G),
    };
    }
}
