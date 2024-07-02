package backend.model;

public class Circle extends Ellipse {

    public Circle(Point centerPoint, double radius) {
        super(centerPoint, radius, radius);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", getCenterPoint(), getsMayorAxis());
    }

    public Figure createNewFigure(Point point1, Point ponit2, double axis1, double axis2, double size) {
        return new Circle(point1, axis1);
    }

    public double getRadius()    {
        return getsMayorAxis();
    }

}
