package d.engine.bodies;

import d.engine.geomety.Point3D;
import d.engine.geomety.Shape3D;
import d.engine.geomety.Triangle;

public class Hexagon extends Shape3D {
    public Hexagon() {
        super(createHexagonalPrism());
    }

    private static Triangle[] createHexagonalPrism() {
        double radius = 1.0;
        double height = 1.0;

        Point3D[] bottom = new Point3D[6];
        Point3D[] top = new Point3D[6];

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            bottom[i] = new Point3D(x, y, -height / 2);
            top[i] = new Point3D(x, y, height / 2);
        }

        Point3D centerBottom = new Point3D(0, 0, -height / 2);
        Point3D centerTop = new Point3D(0, 0, height / 2);

        Triangle[] triangles = new Triangle[6 * 4]; // 6 bottom, 6 top, 12 side triangles
        int idx = 0;

        // Bottom face (triangle fan)
        for (int i = 0; i < 6; i++) {
            triangles[idx++] = new Triangle(centerBottom, bottom[i], bottom[(i + 1) % 6]);
        }

        // Top face (triangle fan)
        for (int i = 0; i < 6; i++) {
            triangles[idx++] = new Triangle(centerTop, top[(i + 1) % 6], top[i]);
        }

        // Side faces (each as 2 triangles)
        for (int i = 0; i < 6; i++) {
            Point3D b1 = bottom[i];
            Point3D b2 = bottom[(i + 1) % 6];
            Point3D t1 = top[i];
            Point3D t2 = top[(i + 1) % 6];

            // First triangle of rectangle
            triangles[idx++] = new Triangle(b1, b2, t1);

            // Second triangle of rectangle
            triangles[idx++] = new Triangle(t1, b2, t2);
        }

        return triangles;
    }
}
