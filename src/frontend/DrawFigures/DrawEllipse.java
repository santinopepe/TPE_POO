package frontend.DrawFigures;

import backend.model.Ellipse;
import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawEllipse extends DrawFigure{

    public DrawEllipse(GraphicsContext gc, FigureProperties figureProperties, Figure figure) {
        super(gc, figureProperties, figure);

    }

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
    public DrawFigure updateFigureProperties(Color color, ShadowType shadow, Color secColor, EdgeType edge, Double width ){
        FigureProperties figureProperties = getFigureProperties();
        figureProperties.setColor(color);
        figureProperties.setShadowType(shadow);
        figureProperties.setSecondaryColor(secColor);
        figureProperties.setEdge(edge);
        figureProperties.setWidth(width);
        return new DrawEllipse(getGc(),figureProperties,getFigure());
    }

}
