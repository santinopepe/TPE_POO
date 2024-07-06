package frontend.Buttons;

import backend.model.Figure;
import backend.model.Point;
import frontend.DrawFigures.DrawFigure;
import frontend.DrawFigures.FigureProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;


//Clase a la que extienden todos los botones que sean figuras
public abstract class CustomButton extends ToggleButton {

    public CustomButton(String name){
        super(name);
    }

    //Devuelve una nueva figura
    public abstract Figure createNewFigure(Point point1, Point point2, double axis1, double axis2, double size);

    //Crea una figura con las características gráficas pedidas
    public abstract DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure);

}
