package d.engine.geomety;

public class Point3D {
    public final double x, y, z;

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

    public boolean equals(Point3D other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z);
    }

}