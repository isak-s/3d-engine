package d.engine.geomety;

public class Triangle {
    private Point3D A;
    private Point3D B;
    private Point3D C;

    public Triangle(Point3D A, Point3D B, Point3D C) {

        // Add some margin for double miscalculations
        if (!isValidTriangle(A, B, C)) {
            throw new IllegalArgumentException("Not a valid triangle");
        }

        this.A = A;
        this.B = B;
        this.C = C;
    }

    public Point3D getA() {
        return A;
    }

    public Point3D getB() {
        return B;
    }

    public Point3D getC() {
        return C;
    }

    public void applyScalar(double scalar) {
        A.applyScalar(scalar);
        B.applyScalar(scalar);
        C.applyScalar(scalar);
    }

    private boolean isValidTriangle(Point3D A, Point3D B, Point3D C) {

        PositionVector3D AB = B.subtract(A);  // Postition vector from a to b
        PositionVector3D AC = C.subtract(A);  // Postition vector from a to c

        // The cross prduct is a Postition vector representing the third side.
        // If its length is 0, it means that AB and AC are parallell -> not a valid triangle.

        return !AB.crossProduct(AC).isZeroVector();
    }
}
