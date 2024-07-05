package frontend.Buttons;

import backend.model.Figure;
import backend.model.Point;
import backend.model.Square;

public class SquareButton extends RectangleButton{

    public SquareButton(String name) {
        super(name);
    }

    @Override
    public Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size) {
        return new Square(point1,size);
    }

}
