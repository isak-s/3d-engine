package d.engine.geomety;

import d.engine.util.Constants;

import java.awt.Point;

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

    public void rotate(double angleX, double angleY) {
        // Rotate around Y axis
        double cosY = Math.cos(angleY);
        double sinY = Math.sin(angleY);
        double x1 = x * cosY + z * sinY;
        double z1 = -x * sinY + z * cosY;

        // Rotate around X axis
        double cosX = Math.cos(angleX);
        double sinX = Math.sin(angleX);
        double y1 = y * cosX - z1 * sinX;
        double z2 = y * sinX + z1 * cosX;

        x = x1;
        y = y1;
        z = z2;
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

    public Point projectOntoScreenPlane() {
        double zClamped = this.z < Constants.EPSILON ? Constants.EPSILON : this.z;

        // Project relative to screen center
        double xProjected = (this.x * Constants.distanceFromObserverToScreen) / zClamped;
        double yProjected = (this.y * Constants.distanceFromObserverToScreen) / zClamped;

        // Shift to screen center
        double screenX = Constants.SCREEN_WIDTH / 2 + xProjected;
        double screenY = Constants.SCREEN_HEIGHT / 2 - yProjected;

        return new Point((int) screenX, (int) screenY);
    }

    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z);
    }

    @Override
    public String toString() {
        return "x: " + x  + " | " + "y: " + y  + " | " + "z: " + z  + " | \n";
    }
}