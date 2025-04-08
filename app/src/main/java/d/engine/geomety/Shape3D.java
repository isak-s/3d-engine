package d.engine.geomety;

import java.awt.geom.Point2D;
import java.util.stream.Stream;

public class Shape3D {
    Triangle[] sides;

    PositionVector3D screenPlaneX = new PositionVector3D(1, 0, 0);
    PositionVector3D screenPlaneY = new PositionVector3D(0, 1, 0);

    public Shape3D(Triangle[] sides) {

        if (!isValidShape()) {
            throw new IllegalArgumentException("Not a valid shape");
        }
        this.sides = sides;
    }

    private boolean isValidShape() {

        Point3D[] allPoints = Stream.of(sides)
            .flatMap(t -> Stream.of(t.getA(), t.getB(), t.getC()))
            .toArray(Point3D[]::new);

        return Stream.of(allPoints).anyMatch(p ->
                    Stream.of(allPoints).filter(p::equals).count() != 3);
    }

    // public projection2d() {
    //     // Point2D
    // }

}