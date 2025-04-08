package d.engine.geomety;

public class Point3D {
    public double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionVector3D subtract(Point3D other) {
        return new PositionVector3D(this.x - other.x,
                                    this.y - other.y,
                                    this.z - other.z);
    }

    public Point3D projectOnto(Point3D) {}

    public void applyScalar(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public boolean equals(Point3D other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z);
    }

}