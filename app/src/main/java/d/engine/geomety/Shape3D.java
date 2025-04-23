package d.engine.geomety;

import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shape3D implements Iterable<Triangle> {

    public Triangle[] faces;

    public Set<Point3D> vertices;

    public PositionVector3D positionVec;

    PositionVector3D screenPlaneX = new PositionVector3D(1, 0, 0);
    PositionVector3D screenPlaneY = new PositionVector3D(0, 1, 0);

    public Shape3D(Triangle[] faces) {

        if (faces.length < 3) {
            throw new IllegalArgumentException("Not enough faces");
        }

        if (!isValidShape()) {
            throw new IllegalArgumentException("Not a valid shape");
        }
        this.faces = faces;

        vertices = Stream.of(faces)
            .flatMap(s -> Stream.of(s.getA(), s.getB(), s.getC()))
            .collect(Collectors.toSet());

        Point3D centroid = computeCentroid();

        vertices.forEach(p -> p.translate(-centroid.x, -centroid.y, -centroid.z));

        positionVec = new PositionVector3D(centroid.x, centroid.y, centroid.z);
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
        return faces.length;
    }

    public void applyScalar(double scalar) {
        vertices.forEach(s -> s.applyScalar(scalar));
    }

    public Point3D computeCentroid() {
        double sumX = 0, sumY = 0, sumZ = 0;
        int count = vertices.size();

        for (Point3D t : vertices) {
            sumX += t.x;
            sumY += t.y;
            sumZ += t.z;
        }

        return new Point3D(sumX / count, sumY / count, sumZ / count);
    }

    public void rotateAroundPoint(Point3D referencePoint, double angleX, double angleY, double angleZ) {
        for (Point3D p : vertices) {
            // Translate to origin
            double translatedX = p.x - referencePoint.x;
            double translatedY = p.y - referencePoint.y;
            double translatedZ = p.z - referencePoint.z;

            // Rotate around Z axis
            double cosZ = Math.cos(angleZ);
            double sinZ = Math.sin(angleZ);
            double xZ = translatedX * cosZ - translatedY * sinZ;
            double yZ = translatedX * sinZ + translatedY * cosZ;
            double zZ = translatedZ;

            // Rotate around Y axis
            double cosY = Math.cos(angleY);
            double sinY = Math.sin(angleY);
            double xY = xZ * cosY + zZ * sinY;
            double zY = -xZ * sinY + zZ * cosY;

            // Rotate around X axis
            double cosX = Math.cos(angleX);
            double sinX = Math.sin(angleX);
            double yX = yZ * cosX - zY * sinX;
            double zX = yZ * sinX + zY * cosX;

            // Translate back
            p.x = xY + referencePoint.x;
            p.y = yX + referencePoint.y;
            p.z = zX + referencePoint.z;
        }
    }

    public void rotateAroundCentroid(double angleX, double angleY, double angleZ) {
        rotateAroundPoint(computeCentroid(), angleX, angleY, angleZ);
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
            return index < faces.length;
        }

        @Override
        public Triangle next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return faces[index++];
        }
    }


}