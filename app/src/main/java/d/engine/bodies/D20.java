package d.engine.bodies;

import d.engine.geometry.Point3D;
import d.engine.geometry.Shape3D;
import d.engine.geometry.Triangle;

public class D20 extends Shape3D {
    public D20() {
        super(createIcosahedron());
    }

    private static Triangle[] createIcosahedron() {
        double t = (1.0 + Math.sqrt(5.0)) / 2.0;  // golden ratio

        // Normalize to unit size
        double scale = 1 / Math.sqrt(1 + t * t);

        double X = scale;
        double Z = t * scale;

        // 12 vertices
        Point3D[] vertices = new Point3D[] {
            new Point3D(-X,  0,  Z),
            new Point3D( X,  0,  Z),
            new Point3D(-X,  0, -Z),
            new Point3D( X,  0, -Z),

            new Point3D( 0,  Z,  X),
            new Point3D( 0,  Z, -X),
            new Point3D( 0, -Z,  X),
            new Point3D( 0, -Z, -X),

            new Point3D( Z,  X,  0),
            new Point3D(-Z,  X,  0),
            new Point3D( Z, -X,  0),
            new Point3D(-Z, -X,  0)
        };

        // 20 faces defined by vertex indices
        int[][] faces = {
            {0, 4, 1}, {0, 9, 4}, {9, 5, 4}, {4, 5, 8}, {4, 8, 1},
            {8, 10, 1}, {8, 3, 10}, {5, 3, 8}, {5, 2, 3}, {2, 7, 3},
            {7, 10, 3}, {7, 6, 10}, {7, 11, 6}, {11, 0, 6}, {0, 1, 6},
            {6, 1, 10}, {9, 0, 11}, {9, 11, 2}, {9, 2, 5}, {7, 2, 11}
        };

        Triangle[] triangles = new Triangle[faces.length];
        for (int i = 0; i < faces.length; i++) {
            triangles[i] = new Triangle(
                vertices[faces[i][0]],
                vertices[faces[i][1]],
                vertices[faces[i][2]]
            );
        }

        return triangles;
    }
}
