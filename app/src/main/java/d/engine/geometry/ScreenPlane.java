package d.engine.geometry;

import d.engine.util.Constants;

public class ScreenPlane extends Plane {

    public PositionVector3D eyePos;

    private double focalLength = Constants.focalLength;

    private double screenWidthMeters;
    private double screenHeightMeters;
    // private int fovYDegrees = 90; // vertical FOV
    private int fovXDegrees = 90; // Horzontal FOV

    public ScreenPlane(PositionVector3D origin, PositionVector3D normal) {
        super(origin, normal);

        // updateFocalLengthFromFOV();
        this.eyePos = origin.subtract(normal.normalized().multiply(focalLength));

        this.u = Constants.SCREEN_PLANE_X;
        this.v = Constants.SCREEN_PLANE_Y;

        calculateScreenSize();

    }

    public ScreenCoordinate projectPointRayCasted(Point3D point) {

    // If the point is behind the observer
    if (point.subtract(eyePos).dotProduct(normal) < Constants.EPSILON) {
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
    if (Math.abs(denominator) < Constants.EPSILON) {
        return null;
    }

    double t =  numerator / denominator;

    //Step 3: Calculate intersection point
    PositionVector3D intersection = eyePos.add(rayDir.multiply(t));

    // System.out.println("Intersection: " + intersection);
    // System.out.println("Distance from camera: " + intersection.magnitude());

    return new ScreenCoordinate(intersection, true);
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

    public void rotateAroundOrigin(double angleX, double angleY, double angleZ) {
        // Translate everything to origin-relative
        PositionVector3D relEye = eyePos.subtract(origin);

        // Apply rotations around origin
        PositionVector3D newRelEye = relEye.rotated(origin, angleX, angleY, angleZ);
        PositionVector3D newNormal = normal.rotated(origin, angleX, angleY, angleZ);
        PositionVector3D newU = u.rotated(origin, angleX, angleY, angleZ);
        PositionVector3D newV = v.rotated(origin, angleX, angleY, angleZ);

        // Translate back
        this.eyePos = origin.add(newRelEye);
        this.normal = newNormal.normalized();
        this.u = newU.normalized();
        this.v = newV.normalized();
    }

    private void calculateScreenSize() {
        double fovXRadians = Math.toRadians(fovXDegrees);
        screenHeightMeters = 2 * focalLength * Math.tan(fovXRadians / 2);
        screenWidthMeters = screenHeightMeters * Constants.SCREEN_WIDTH / Constants.SCREEN_HEIGHT;
    }

    private void updateFocalLengthFromFOV() {
        // Use vertical FOV and screen height in pixels to compute focal length in meters
        double fovXRadians = Math.toRadians(fovXDegrees);
        this.focalLength = (Constants.SCREEN_HEIGHT / 2.0) / Math.tan(fovXRadians / 2.0);

        // Convert from pixels to meters using a scale (assume 1 pixel = 1 meter for now, or apply a constant if needed)
    }

    public void setFovX(int fovXDegrees) {
        this.fovXDegrees = fovXDegrees;
        updateFocalLengthFromFOV();
        updateEyePos();
        calculateScreenSize();
    }

    public int getFovX() {
        return fovXDegrees;
    }

    @Override
    public void setOrigin(PositionVector3D p) {
        this.origin = new PositionVector3D(p.x, p.y, p.z);
        updateEyePos();
    }

    // Movement /////////////////////////////////////

    public void moveForward(int units) {
        PositionVector3D delta = normal.multiply(units);
        eyePos = eyePos.add(delta);
        origin = origin.add(delta);
    }

    public void moveBackward(int units) {
        PositionVector3D delta = normal.multiply(-units);
        eyePos = eyePos.add(delta);
        origin = origin.add(delta);
    }

    public void moveRight(int units) {
        PositionVector3D delta = u.multiply(units);
        eyePos = eyePos.add(delta);
        origin = origin.add(delta);
    }

    public void moveLeft(int units) {
        PositionVector3D delta = u.multiply(-units);
        eyePos = eyePos.add(delta);
        origin = origin.add(delta);
    }

    public void moveUp(int units) {
        PositionVector3D delta = v.multiply(units);
        eyePos = eyePos.add(delta);
        origin = origin.add(delta);
    }

    public void moveDown(int units) {
        PositionVector3D delta = v.multiply(-units);
        eyePos = eyePos.add(delta);
        origin = origin.add(delta);
    }

    private void updateEyePos() {
        this.eyePos = origin.subtract(normal.normalized().multiply(focalLength));
    }

    ///////////////////////////////////////////////


    public ScreenCoordinate screenCoordinate(PositionVector3D point) {
        return new ScreenCoordinate(point);
    }

    public class ScreenCoordinate {

        private int x;
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
        @Override
        public String toString() {
            return "x: " + x + "y: " + y ;
        }

        private ScreenCoordinate(PositionVector3D point, Boolean fovBased) {
            PositionVector3D rel = point.subtract(origin);

            double xMeters = rel.dotProduct(u);  // projected X coordinate on plane
            double yMeters = rel.dotProduct(v);  // projected Y coordinate on plane

            // Convert meters to pixels
            int xPixels = (int) Math.round(
                Constants.SCREEN_WIDTH * (xMeters / screenWidthMeters + 0.5)
            );

            int yPixels = (int) Math.round(
                Constants.SCREEN_HEIGHT * (0.5 - yMeters / screenHeightMeters)
            );

            this.x = xPixels;
            this.y = yPixels;
        }
    }
}
