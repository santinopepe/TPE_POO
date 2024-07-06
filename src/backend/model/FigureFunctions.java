package backend.model;

public interface FigureFunctions{

    //Calcula la diferecia en X e Y de los puntos de la figura
    double figureCalcXCoord();
    double figureCalcYCoord();

    //devuelve verdadero si el punto pertenece a la figura, falso en caso contrario
    boolean belongs(Point eventPoint);

    //mueve la coordenada X o Y de la figura aplicándole el parámetro diff
    void moveCoordX(double diff);
    void moveCoordY(double diff);

    //Devuelve los puntos de la figura, si es una elipse/circulo ambos devuelven el centro
    Point getPoint1();
    Point getPoint2();

    //Devuelven los ejes de las figuras
    double getAxis1();
    double getAxis2();


    Figure createDividedFigure(Point point1, Point point2, Point centerP,double axis1, double axis2);

    //mueve la figura al centro del lienzo
    void centerFigure(double canvasWidth, double canvasHeight);
}
