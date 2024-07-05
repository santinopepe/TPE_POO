package frontend.Buttons;

import backend.model.Figure;
import backend.model.Point;
import backend.model.Rectangle;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.DrawRect;
import frontend.DrawFigures.FigureProperties;
import javafx.scene.canvas.GraphicsContext;

public class RectangleButton extends CustomButton{

    public RectangleButton(String name) {
        super(name);
    }

    @Override
    public Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size) {
        return new Rectangle(point1,point2);
    }

    @Override
    public DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure) {
        return new DrawRect(gc,fProperties,figure);
    }
}

