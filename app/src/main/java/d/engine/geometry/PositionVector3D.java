package d.engine.geometry;

// ortsvektor på svenska
public class PositionVector3D extends Point3D {

    public PositionVector3D(double x, double y, double z) {
        super(x, y, z);
    }

    public double dotProduct(Point3D o) {
        return this.x * o.x + this.y * o.y + this.z *o.z;
    }

    public PositionVector3D crossProduct(Point3D o) {
        return new PositionVector3D(
                this.y*o.z - this.z*o.y,
                this.z*o.x - this.x*o.z,
                this.x*o.y - this.y*o.x
        );
    }

    public boolean isZeroVector() {
        double tolerance = 1e-10;
        return (Math.abs(x) < tolerance &&
                Math.abs(y) < tolerance &&
                Math.abs(z) < tolerance
            );
    }

    public PositionVector3D normalized() {
        double scalar = magnitude();

        return new PositionVector3D(x / scalar, y / scalar, z / scalar);
    }

    @Override
    public PositionVector3D add(Point3D other) {
        return new PositionVector3D(x + other.x, y + other.y, z + other.z);
    }

    public PositionVector3D multiply(double scalar) {
        PositionVector3D newVec = new PositionVector3D(x, y, z);
        newVec.applyScalar(scalar);
        return newVec;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public PositionVector3D rotated(Point3D pivot, double angleX, double angleY, double angleZ) {
        PositionVector3D copy = new PositionVector3D(x, y, z);
        copy.rotate(pivot, angleX, angleY, angleZ);
        return copy;
    }

}
