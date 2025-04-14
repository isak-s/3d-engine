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


    public boolean equals(Point3D other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public Point projectOntoScreenPlane() {
        double zClamped = this.z < Constants.EPSILON ? Constants.EPSILON : this.z;

        double xProjected = (this.x * Constants.distanceFromObserverToScreen) / zClamped;
        double yProjected = (this.y * Constants.distanceFromObserverToScreen) / zClamped;

        int screenX = (int) ((xProjected + 1) * Constants.SCREEN_WIDTH / 2);
        int screenY = (int) ((1 - yProjected) * Constants.SCREEN_HEIGHT / 2);

        return new Point(screenX, screenY);
    }

    public int hashCode() {
        return Double.hashCode(x) ^ Double.hashCode(y) ^ Double.hashCode(z);
    }

}