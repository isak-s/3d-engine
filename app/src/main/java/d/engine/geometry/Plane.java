package d.engine.geomety;


public class Plane {
    PositionVector3D origin;   // A point on the plane
    PositionVector3D normal;   // Normal vector (unit length)
    PositionVector3D u;        // Local X axis (unit vector, lies on plane)
    PositionVector3D v;        // Local Y axis (unit vector, lies on plane)

    public Plane(PositionVector3D origin, PositionVector3D normal) {
        this.origin = origin;
        this.normal = normal.normalized();
        this.u = generatePerpendicularVector(this.normal).normalized();
        this.v = this.normal.crossProduct(u).normalized();
    }

    public PositionVector3D projectPoint(PositionVector3D point) {

        // calculate position vector from origin (a point on the plane) to the point.
        PositionVector3D fromOrigin = point.subtract(origin);

        // Ditance from origin to point
        double dist = fromOrigin.dotProduct(normal);

        // multiply normal with distance. Vector orthogonal from the plane to the point.
        PositionVector3D planeToPoint = new PositionVector3D(normal.x, normal.y, normal.z);
        planeToPoint.applyScalar(dist);

        // remove the orthogonal (relative to the plane) part of the point.
        PositionVector3D projected = point.subtract(planeToPoint);

        // position of the projected point relative to the plane's origin.
        PositionVector3D rel = projected.subtract(origin);

        // Projection of relative vector onto local basis vectors.
        double x = rel.dotProduct(u);
        double y = rel.dotProduct(v);
        // 2D Position vector with origin as its starting point.
        //      |y
        //      |   o
        //      |  /
        //      | /
        //      |/___________x
        return new PositionVector3D(x, y, 0); // Projected onto the plane
    }

    public void setOrigin(PositionVector3D origin) {
        this.origin = origin;
    }

    public PositionVector3D getOrigin() {
        return this.origin;
    }


    private PositionVector3D generatePerpendicularVector(PositionVector3D v) {
        // Pick any non-parallel vector and cross it with v
        if (Math.abs(v.x) < Math.abs(v.y) && Math.abs(v.x) < Math.abs(v.z))
            return new PositionVector3D(0, -v.z, v.y); // Cross with X
        else if (Math.abs(v.y) < Math.abs(v.z))
            return new PositionVector3D(-v.z, 0, v.x); // Cross with Y
        else
            return new PositionVector3D(-v.y, v.x, 0); // Cross with Z
    }

}