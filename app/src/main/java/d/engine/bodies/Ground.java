package d.engine.bodies;

import java.util.ArrayList;
import java.util.List;

import d.engine.geometry.Point3D;
import d.engine.geometry.Shape3D;
import d.engine.geometry.Triangle;

public class Ground extends Shape3D {

    public Ground(int dim) {
        super(createGroundFaces(dim));
    }

    private static Triangle[] createGroundFaces(int dim) {
        List<Triangle> faces = new ArrayList<>();

        for (int x = -dim; x < dim; x++) {
            for (int z = -dim; z < dim; z++) {
                faces.addAll(createSquare(x, z));
            }
        }

        return faces.toArray(new Triangle[0]);
    }

    private static List<Triangle> createSquare(int x, int z) {
        List<Triangle> tris = new ArrayList<>();

        Point3D p1 = new Point3D(x, 0, z);
        Point3D p2 = new Point3D(x + 1, 0, z);
        Point3D p3 = new Point3D(x, 0, z + 1);
        Point3D p4 = new Point3D(x + 1, 0, z + 1);

        tris.add(new Triangle(p1, p2, p3));
        tris.add(new Triangle(p2, p4, p3));

        return tris;
    }
}