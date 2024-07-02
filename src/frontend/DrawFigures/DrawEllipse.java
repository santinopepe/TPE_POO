package frontend.DrawFigures;

import backend.ShadowType;
import backend.model.Ellipse;
import backend.model.Figure;
import backend.model.FigureProperties;
import javafx.scene.canvas.GraphicsContext;

public class DrawEllipse extends DrawFigure{

    public DrawEllipse(GraphicsContext gc, FigureProperties figureProperties, Figure figure) {
        super(gc, figureProperties, figure);

    }

    //Es necesario el casteo?
    @Override
    public void drawFigure() {
        Ellipse ellipse = (Ellipse) getFigure();
        setEdge();
        if(!getFigureProperties().getShadowType().equals(ShadowType.NONE)){
            setOvalShadow(ellipse.getCenterPoint(), ellipse.getsMayorAxis(), ellipse.getsMinorAxis() );
        }
        setOvalGradient();
        if(areEqual()){
            getGc().setFill(getFigureProperties().getColor());
        }
        getGc().strokeOval(ellipse.figureCalcXCoord(), ellipse.figureCalcYCoord(),
                ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
        getGc().fillOval(ellipse.figureCalcXCoord(), ellipse.figureCalcYCoord(),
                ellipse.getsMayorAxis(), ellipse.getsMinorAxis());

    }

    @Override
    public DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure) {
        return new DrawEllipse(gc,fProperties,figure);
    }


/*
    //Chequear si se le puede pasar menos paramentros.
    @Override
    public DrawFigure createFigure(Figure fig, FigureProperties prop) {
        return new DrawEllipse(getGc(),prop,fig);
    }
*/
}
