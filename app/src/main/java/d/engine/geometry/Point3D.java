package d.engine.geometry;

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

    public void applyScalar(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public void translate(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    public Point3D add(Point3D other) {
        return new Point3D(x + other.x, y + other.y, z + other.z);
    }
    public PositionVector3D add(PositionVector3D other) {
        return new PositionVector3D(x + other.x, y + other.y, z + other.z);
    }

    public void rotate(Point3D pivot, double angleX, double angleY, double angleZ) {

        this.translate(-pivot.x, -pivot.y, -pivot.z);

        // Rotate around Z axis
        double cosZ = Math.cos(angleZ);
        double sinZ = Math.sin(angleZ);
        double xZ = x * cosZ - y * sinZ;
        double yZ = x * sinZ + y * cosZ;
        double zZ = z;

        // Rotate around Y axis
        double cosY = Math.cos(angleY);
        double sinY = Math.sin(angleY);
        double xY = xZ * cosY + zZ * sinY;
        double zY = -xZ * sinY + zZ * cosY;
        double yY = yZ;

        // Rotate around X axis
        double cosX = Math.cos(angleX);
        double sinX = Math.sin(angleX);
        double yX = yY * cosX - zY * sinX;
        double zX = yY * sinX + zY * cosX;

        x = xY;
        y = yX;
        z = zX;

        this.translate(pivot.x, pivot.y, pivot.z);
    }

    public Point3D rotated(Point3D pivot, double angleX, double angleY, double angleZ) {
        Point3D copy = new Point3D(x, y, z);
        copy.rotate(pivot, angleX, angleY, angleZ);
        return copy;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point3D other = (Point3D) obj;
        return Double.compare(x, other.x) == 0 &&
               Double.compare(y, other.y) == 0 &&
               Double.compare(z, other.z) == 0;
    }

    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z);
    }

    @Override
    public String toString() {
        return "x: " + x  + " | " + "y: " + y  + " | " + "z: " + z  + " | \n";
    }
}