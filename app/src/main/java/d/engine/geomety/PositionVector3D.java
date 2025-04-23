package d.engine.geomety;

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
        double scalar = Math.sqrt(x*x + y*y + z*z);

        return new PositionVector3D(x / scalar, y / scalar, z / scalar);
    }

}
