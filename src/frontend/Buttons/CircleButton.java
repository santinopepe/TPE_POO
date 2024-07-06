package frontend.Buttons;

import backend.model.Circle;
import backend.model.Figure;
import backend.model.Point;

public class CircleButton extends EllipseButton{

    public CircleButton(String name) {
        super(name);
    }

    @Override
    public Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size) {
        double radius = Math.abs(point2.getX() - point1.getX());
        return new Circle(point1, radius+axis1);
    }
}
