package backend.model;

public interface Figure {
    double figureCalcXCoord();
    double figureCalcYCoord();
    boolean belongs(Point eventPoint);
    void moveCoordX(double diff);
    void moveCoordY(double diff);
    Figure createNewFigure(Point point1, Point ponit2, double axis1, double axis2, double size);

}
