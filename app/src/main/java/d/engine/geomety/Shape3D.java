package d.engine.geomety;

import java.util.stream.Stream;

public class Shape3D {
    Triangle[] sides;

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
}