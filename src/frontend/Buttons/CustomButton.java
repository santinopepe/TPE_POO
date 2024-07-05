package frontend.Buttons;

import backend.model.Figure;
import backend.model.Point;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.FigureProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;

public abstract class CustomButton extends ToggleButton {

    public CustomButton(String name){
        super(name);
    }

    public abstract Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size);

    public abstract DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure);

}
