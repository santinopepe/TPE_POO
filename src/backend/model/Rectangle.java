package backend.model;

import java.util.Objects;

public class Rectangle implements Figure{

    private final Point topLeft;
    private final Point bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

    @Override
    public double figureCalcXCoord() {
        return Math.abs(topLeft.getX()-bottomRight.getX());
    }

    @Override
    public double figureCalcYCoord() {
        return Math.abs(topLeft.getY() - bottomRight.getY());
    }

    @Override
    public boolean belongs(Point eventPoint) {
        return eventPoint.getX() > this.getTopLeft().getX() && eventPoint.getX() < this.getBottomRight().getX() &&
                eventPoint.getY() > this.getTopLeft().getY() && eventPoint.getY() < this.getBottomRight().getY();
    }

    @Override
    public void moveCoordX(double diff) {
        topLeft.moveX(diff);
        bottomRight.moveX(diff);
    }

    @Override
    public void moveCoordY(double diff) {
        topLeft.moveY(diff);
        bottomRight.moveY(diff);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle rectangle)) return false;
        return topLeft.equals(rectangle.topLeft) && bottomRight.equals(rectangle.bottomRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeft, bottomRight);
    }

    @Override
    public Point getPoint1() {
        return getTopLeft();
    }

    @Override
    public Point getPoint2() {
        return getBottomRight();
    }

    @Override
    public double getAxis1() {
        return 0;
    }

    @Override
    public double getAxis2() {
        return 0;
    }

    @Override
    public Figure createDividedFigure(Point point1, Point point2, Point centerP, double axis1, double axis2) {
        return new Rectangle(point1, point2);
    }

    @Override
    public void centerFigure(double canvasWidth, double canvasHeight) {
        double rectWidth = Math.abs(topLeft.getX() - bottomRight.getX());
        double rectHeight = Math.abs(topLeft.getY() - bottomRight.getY());
        double newTopLeftX = (canvasWidth - rectWidth) / 2;
        double newTopLeftY = (canvasHeight - rectHeight) / 2;

        topLeft.setX(newTopLeftX);
        topLeft.setY(newTopLeftY);
        bottomRight.setX(newTopLeftX + rectWidth);
        bottomRight.setY(newTopLeftY + rectHeight);
    }

}
