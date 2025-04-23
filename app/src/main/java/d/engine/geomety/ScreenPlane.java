package d.engine.geomety;

import d.engine.util.Constants;

public class ScreenPlane extends Plane {

    PositionVector3D eyePos;

    public ScreenPlane(PositionVector3D origin, PositionVector3D normal) {
        super(origin, normal);

        eyePos = new PositionVector3D(origin.x, origin.y, origin.z - Constants.focalLength);
    }

    public ScreenCoordinate projectPointRayCasted(Point3D point) {

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
    PositionVector3D intersection = eyePos.add(rayDir);

    return new ScreenCoordinate(intersection);
    }


    public class ScreenCoordinate {

        private int x; // integer instead?
        private int y;

        private ScreenCoordinate(PositionVector3D point) {
            PositionVector3D rel = point.subtract(origin);

            this.x = Constants.SCREEN_WIDTH/2 + (int) Math.round(rel.dotProduct(u));
            this.y = Constants.SCREEN_HEIGHT/2 - (int) Math.round(rel.dotProduct(v));  // y is inverted
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
}
