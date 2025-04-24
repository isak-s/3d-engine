package d.engine.geomety;

import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shape3D implements Iterable<Triangle> {

    private final Triangle[] originalFaces;  // never modified

    public Triangle[] faces;

    public Set<Point3D> vertices;

    public PositionVector3D positionVec;

    public Shape3D(Triangle[] faces) {

        if (faces.length < 3) {
            throw new IllegalArgumentException("Not enough faces");
        }

        this.faces = deepCloneTriangles(faces); // working copy
        updateVertices();

        if (!isValidShape()) {
            throw new IllegalArgumentException("Not a valid shape");
        }

        Point3D centroid = computeCentroid();

        vertices.forEach(p -> p.translate(-centroid.x, -centroid.y, -centroid.z));

        this.originalFaces = deepCloneTriangles(this.faces);

        positionVec = new PositionVector3D(centroid.x, centroid.y, centroid.z);
    }

    public void resetTransformations() {
        this.faces = deepCloneTriangles(originalFaces);
        updateVertices();
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
            p = p.rotated(referencePoint, angleX, angleY, angleZ);
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

    private Triangle[] deepCloneTriangles(Triangle[] source) {
        return Arrays.stream(source).map(t ->
            new Triangle(
                new Point3D(t.getA().x, t.getA().y, t.getA().z),
                new Point3D(t.getB().x, t.getB().y, t.getB().z),
                new Point3D(t.getC().x, t.getC().y, t.getC().z)
            )
        ).toArray(Triangle[]::new);
    }

    private void updateVertices() {
        this.vertices = Stream.of(faces)
            .flatMap(t -> Stream.of(t.getA(), t.getB(), t.getC()))
            .collect(Collectors.toSet());
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