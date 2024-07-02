package frontend.DrawFigures;

import backend.ShadowType;
import backend.model.*;
import backend.model.Rectangle;

import javafx.scene.canvas.GraphicsContext;


public class DrawRect extends DrawFigure{

    public DrawRect(GraphicsContext gc, FigureProperties figureProperties, Figure figure) {
        super(gc, figureProperties, figure);
    }

    //Es necesario el casteo?
    @Override
    public void drawFigure() {
        Rectangle rectangle = (Rectangle) getFigure();
        setEdge();
        if(!getFigureProperties().getShadowType().equals(ShadowType.NONE)){
            setRectangleShadow(rectangle.getTopLeft(),rectangle.getBottomRight());
        }
        setRectangleGradient();
        if(areEqual()){
            getGc().setFill(getFigureProperties().getColor());
        }
        getGc().fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
                rectangle.figureCalcXCoord(), rectangle.figureCalcYCoord());
        getGc().strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
                rectangle.figureCalcXCoord(), rectangle.figureCalcYCoord());
    }

    @Override
    public DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure) {
        return new DrawRect(gc,fProperties,figure);
    }



/*
    //Chequear si se le puede pasar menos paramentros.
    @Override
    public DrawFigure createDrawFigure(GraphicsContext gc, FigureProperties prop) {
        return new DrawRect(getGc(),prop,this);
    }

 */

}
