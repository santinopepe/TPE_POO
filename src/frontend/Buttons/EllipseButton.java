package frontend.Buttons;

import backend.model.*;
import backend.model.Point;
import frontend.DrawFigures.DrawEllipse;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.FigureProperties;
import javafx.scene.canvas.GraphicsContext;

public class EllipseButton extends CustomButton{

    public EllipseButton(String name){
        super(name);
    }

    public Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size){
        Point center = new Point(point1.getX()+((point2.getX()-point1.getX())/2),point1.getY()+((point2.getY()-point1.getY())/2));
        return new Ellipse(center, axis1, axis2);
    }

    @Override
    public DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure) {
        return new DrawEllipse(gc,fProperties,figure);
    }
}
