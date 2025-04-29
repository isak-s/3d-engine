package d.engine.geometry;

import d.engine.util.Constants;

public class ScreenPlane extends Plane {

    PositionVector3D eyePos;

    public ScreenPlane(PositionVector3D origin, PositionVector3D normal) {
        super(origin, normal);

        eyePos = new PositionVector3D(origin.x, origin.y, origin.z - Constants.focalLength);

        this.u = new PositionVector3D(1, 0, 0);
        this.v = new PositionVector3D(0, 1, 0);
    }

    public ScreenCoordinate projectPointRayCasted(Point3D point) {

    // If the point is behind the observer
    if (point.z - origin.z < Constants.EPSILON) {
        return null;
    }

    // Step 1: Construct ray from eye to point
    PositionVector3D rayDir = point.subtract(eyePos).normalized(); // direction of ray

    // Step 2: Compute intersection with plane
    // Plane equation: (P - origin) ⋅ normal = 0
    // Ray: P = eye + t * rayDir
    // => ((eye + t * rayDir) - origin) ⋅ normal = 0
    // => (eye - origin + t * rayDir) ⋅ normal = 0
    // Solve for t

    PositionVector3D eyeToOrigin = eyePos.subtract(origin);
    double numerator = -eyeToOrigin.dotProduct(normal);
    double denominator = rayDir.dotProduct(normal);

    // Avoid division by zero — ray is parallel to plane
    if (Math.abs(denominator) < 1e-6) {
        return null;
    }

    double t = numerator / denominator;

    //Step 3: Calculate intersection point
    PositionVector3D eyeToPoint = new PositionVector3D(rayDir.x, rayDir.y, rayDir.z);
    eyeToPoint.applyScalar(t);
    PositionVector3D intersection = eyePos.add(eyeToPoint);

    // System.out.println("Distance to point: " + point.subtract(eyePos).magnitude());

    return new ScreenCoordinate(intersection);
    }

    public void rotateAroundPoint(Point3D pivot, double angleX, double angleY, double angleZ) {
        // Translate everything to origin relative to pivot
        PositionVector3D relEye = eyePos.subtract(pivot);
        PositionVector3D relOrigin = origin.subtract(pivot);

        // Apply rotations
        PositionVector3D newRelEye = relEye.rotated(pivot, angleX, angleY, angleZ);
        PositionVector3D newRelOrigin = relOrigin.rotated(pivot, angleX, angleY, angleZ);
        PositionVector3D newNormal = normal.rotated(pivot, angleX, angleY, angleZ);
        PositionVector3D newU = u.rotated(pivot, angleX, angleY, angleZ);
        PositionVector3D newV = v.rotated(pivot, angleX, angleY, angleZ);

        // Translate back
        this.eyePos = pivot.add(newRelEye);
        this.origin = pivot.add(newRelOrigin);
        this.normal = newNormal.normalized();
        this.u = newU.normalized();
        this.v = newV.normalized();
    }

    public ScreenCoordinate ScreenCoordinateFromPositionVector(PositionVector3D p) {
        return new ScreenCoordinate(p);
    }

    public PositionVector3D getPos() {
        return origin;
    }

    public void setFocalLength(double focalLength) {
        this.eyePos = new PositionVector3D(origin.x, origin.y, origin.z - focalLength);
    }



    public class ScreenCoordinate {

        private int x;
        private int y;

        private ScreenCoordinate(PositionVector3D point) {
            PositionVector3D rel = point; // point.subtract(origin);

            this.x = Constants.SCREEN_WIDTH/2 + (int) Math.round(rel.dotProduct(u));
            this.y = Constants.SCREEN_HEIGHT/2 - (int) Math.round(rel.dotProduct(v));  // y is inverted
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        @Override
        public String toString() {
            return "x: " + x + "y: " + y ;
        }
    }
}
