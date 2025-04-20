package d.engine.geomety;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class Shape3D implements Iterable<Triangle> {
    public Triangle[] sides;

    PositionVector3D screenPlaneX = new PositionVector3D(1, 0, 0);
    PositionVector3D screenPlaneY = new PositionVector3D(0, 1, 0);

    public Shape3D(Triangle[] sides) {

        if (sides.length < 3) {
            throw new IllegalArgumentException("Not enough faces");
        }

        if (!isValidShape()) {
            throw new IllegalArgumentException("Not a valid shape");
        }
        this.sides = sides;
    }

    private boolean isValidShape() {

        // Point3D[] allPoints = Stream.of(sides)
        //     .flatMap(t -> Stream.of(t.getA(), t.getB(), t.getC()))
        //     .toArray(Point3D[]::new);

        // return Stream.of(allPoints).anyMatch(p ->
        //             Stream.of(allPoints).filter(p::equals).count() != 3);
        return true;
    }

    public int getNbrFaces() {
        return sides.length;
    }

    public Point3D computeCentroid() {
        double sumX = 0, sumY = 0, sumZ = 0;
        int count = 0;

        for (Triangle t : sides) {
            sumX += t.getA().x + t.getB().x + t.getC().x;
            sumY += t.getA().y + t.getB().y + t.getC().y;
            sumZ += t.getA().z + t.getB().z + t.getC().z;
            count += 3;
        }

        return new Point3D(sumX / count, sumY / count, sumZ / count);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Triangle s : sides) {
            sb.append(s.getA().toString() + " " + s.getB().toString() + " " + s.getC().toString());
        }
        return sb.toString();
    }

    @Override
    public Iterator<Triangle> iterator() {
        return new TriangleIterator();
    }


    private class TriangleIterator implements Iterator<Triangle> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < sides.length;
        }

        @Override
        public Triangle next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return sides[index++];
        }
    }


}