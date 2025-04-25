package d.engine.bodies;

import d.engine.geometry.Point3D;
import d.engine.geometry.Shape3D;
import d.engine.geometry.Triangle;

public class Tetrahedron extends Shape3D {

    public Tetrahedron() {

        super(createTetrhedronFaces());
    }

    private static Triangle[] createTetrhedronFaces() {

        Point3D A = new Point3D(-0.5109, -0.1868, -0.1903);
        Point3D B = new Point3D( 0.4766, -0.3599, -0.2167);
        Point3D C = new Point3D(-0.0930,  0.5763, -0.1996);
        Point3D D = new Point3D( 0.0502,  0.0827,  0.6136);

        // Define the faces (triangles)
        Triangle triangle1 = new Triangle(A, B, C);  // Face ABC
        Triangle triangle2 = new Triangle(A, B, D);  // Face ABD
        Triangle triangle3 = new Triangle(B, C, D);  // Face BCD
        Triangle triangle4 = new Triangle(A, C, D);  // Face ACD

        return new Triangle[] {triangle1, triangle2, triangle3, triangle4};
    }
}
