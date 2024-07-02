package backend.model;

public interface FigureFunctions {
    double figureCalcXCoord();
    double figureCalcYCoord();
    boolean belongs(Point eventPoint);
    void moveCoordX(double diff);
    void moveCoordY(double diff);
    Point getPoint1();
    Point getPoint2();
    double getAxis1();
    double getAxis2();
    Figure createDividedFigure(Point point1, Point point2, Point centerP,double axis1, double axis2);
    Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size);
    void centerFigure(double canvasWidth, double canvasHeight);
}
