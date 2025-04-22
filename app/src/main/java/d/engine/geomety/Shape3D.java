package d.engine.geomety;

import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shape3D implements Iterable<Triangle> {

    public Triangle[] sides;

    public Set<Point3D> vertices;

    public PositionVector3D positionVec;

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

        vertices = Stream.of(sides)
            .flatMap(s -> Stream.of(s.getA(), s.getB(), s.getC()))
            .collect(Collectors.toSet());

        Point3D centroid = computeCentroid();

        positionVec = new PositionVector3D(-centroid.x, -centroid.y, 25 - centroid.z);
    }

    public void setPosition(PositionVector3D p) {
        positionVec = p;
    }

    public PositionVector3D getPosition() {
        return positionVec;
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

    public void applyScalar(double scalar) {
        vertices.forEach(s -> s.applyScalar(scalar));
    }

    public Point3D computeCentroid() {
        double sumX = 0, sumY = 0, sumZ = 0;
        int count = 0;

        for (Point3D t : vertices) {
            sumX += t.x;
            sumY += t.y;
            sumZ += t.z;
            count += 1;
        }

        return new Point3D(sumX / count, sumY / count, sumZ / count);
    }

    public void rotateAroundPoint(Point3D referencePoint, double angleX, double angleY) {
        for (Point3D p : vertices) {
            // Translate to origin
            p.x -= referencePoint.x;
            p.y -= referencePoint.y;
            p.z -= referencePoint.z;

            // Rotate around Y
            double cosY = Math.cos(angleY);
            double sinY = Math.sin(angleY);
            double x1 = p.x * cosY + p.z * sinY;
            double z1 = -p.x * sinY + p.z * cosY;

            // Rotate around X
            double cosX = Math.cos(angleX);
            double sinX = Math.sin(angleX);
            double y1 = p.y * cosX - z1 * sinX;
            double z2 = p.y * sinX + z1 * cosX;

            // Update
            p.x = x1 + referencePoint.x;
            p.y = y1 + referencePoint.y;
            p.z = z2 + referencePoint.z;
        }
    }

    public void rotateAroundCentroid(double angleX, double angleY) {
        rotateAroundPoint(computeCentroid(), angleX, angleY);
    }

    public void moveCentroidTo(Point3D target) {
        Point3D current = computeCentroid();

        double dx = target.x - current.x;
        double dy = target.y - current.y;
        double dz = target.z - current.z;

        vertices.forEach(p -> p.translate(dx, dy, dz));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Point3D s : vertices) {
            sb.append(s.toString());
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