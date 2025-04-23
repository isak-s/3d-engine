package d.engine.geomety;

import d.engine.util.Constants;

public class ScreenPlane extends Plane {

    PositionVector3D eyePos;

    public ScreenPlane(PositionVector3D origin, PositionVector3D normal) {
        super(origin, normal);

        eyePos = new PositionVector3D(origin.x, origin.y, origin.z - Constants.focalLength);
    }

    @Override
    public PositionVector3D projectPoint(PositionVector3D point) {

    // Step 1: Construct ray from eye to point
    PositionVector3D rayDir = point.subtract(eyePos).normalized(); // direction of ray

    // Step 2: Compute intersection with plane
    // Plane equation: (P - origin) ⋅ normal = 0
    // Ray: P = eye + t * rayDir
    // => ((eye + t * rayDir) - origin) ⋅ normal = 0
    // => (eye - origin + t * rayDir) ⋅ normal = 0
    // Solve for t
    }


    private class screenCoordinate {

        private double x; // integer instead?
        private double y;

        public screenCoordinate(PositionVector3D point) {
            PositionVector3D rel = point.subtract(origin);

            this.x = rel.x;
            this.y = rel.y;
        }

        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
    }
}
