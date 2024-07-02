package backend.model;

import java.util.Objects;

public class Ellipse implements Figure {

    private final Point centerPoint;
    private final double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ellipse ellipse)) return false;
        return Double.compare(sMayorAxis, ellipse.sMayorAxis) == 0 &&
                Double.compare(sMinorAxis, ellipse.sMinorAxis) == 0 && centerPoint.equals(ellipse.centerPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerPoint, sMayorAxis, sMinorAxis);
    }

    @Override
    public double figureCalcXCoord() {
        return centerPoint.getX() - (sMayorAxis / 2) ;
    }

    @Override
    public double figureCalcYCoord() {
        return centerPoint.getY() - (sMinorAxis / 2) ;
    }

    @Override
    public boolean belongs(Point eventPoint){
        return ((Math.pow(eventPoint.getX() - this.getCenterPoint().getX(), 2) / Math.pow(this.getsMayorAxis(), 2)) +
                (Math.pow(eventPoint.getY() - this.getCenterPoint().getY(), 2) / Math.pow(this.getsMinorAxis(), 2))) <= 0.30;
    }


    @Override
    public void moveCoordX(double diff) {
        centerPoint.moveX(diff);
    }

    @Override
    public void moveCoordY(double diff) {
        centerPoint.moveY(diff);
    }

    @Override
    public Figure createNewFigure(Point point1, Point ponit2, double axis1, double axis2, double size) {
        return new Ellipse(point1, axis1, axis2);
    }
}
