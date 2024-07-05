package frontend.DrawFigures;

import backend.ShadowType;
import backend.model.*;
import backend.model.Rectangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class DrawRect extends DrawFigure{

    public DrawRect(GraphicsContext gc, FigureProperties figureProperties, Figure figure) {
        super(gc, figureProperties, figure);
    }


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

    public DrawFigure updateFigureProperties(Color color, ShadowType shadow, Color secColor, EdgeType edge, Double width ){
        FigureProperties figureProperties = getFigureProperties();
        figureProperties.setColor(color);
        figureProperties.setShadowType(shadow);
        figureProperties.setSecondaryColor(secColor);
        figureProperties.setEdge(edge);
        figureProperties.setWidth(width);
        return new DrawRect(getGc(),figureProperties,getFigure());
    }




}
