package d.engine.util;

import d.engine.geometry.PositionVector3D;

public final class Constants {

    // Prevent instantiation
    private Constants() {}

    public static final double EPSILON = 1e-6;
    public static final int SCREEN_WIDTH = 16 *100;
    public static final int SCREEN_HEIGHT = 9 * 100;
    public static final String ENGINE_VERSION = "1.0.0";

    public static final PositionVector3D SCREEN_PLANE_X = new PositionVector3D(1, 0, 0);
    public static final PositionVector3D SCREEN_PLANE_Y = new PositionVector3D(0, 1, 0);

    public static final double distanceFromObserverToScreen = 25;
    public static double focalLength = 0.25;  // meters

}