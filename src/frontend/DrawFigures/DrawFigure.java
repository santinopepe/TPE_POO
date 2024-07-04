package frontend.DrawFigures;

import backend.ShadowType;
import backend.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;


public abstract class DrawFigure{

    private GraphicsContext gc;
    private final FigureProperties figureProperties;
    private final Figure figure;

    protected DrawFigure(GraphicsContext gc, FigureProperties figureProperties, Figure figure) {
        this.gc = gc;
        this.figureProperties = figureProperties;
        this.figure = figure;
    }

    public void setOvalShadow(Point centerPoint, double mayorAxis, double minorAxis){
        if(!figureProperties.getShadowType().equals(ShadowType.NONE)){
            Ellipse shadow = new Ellipse(centerPoint, mayorAxis, minorAxis);
            gc.setFill(figureProperties.getShadowType().getColor(figureProperties.getColor()));
            gc.fillOval(figureProperties.getShadowType().shadowCoordCalc(centerPoint.getX(),mayorAxis),
                    figureProperties.getShadowType().shadowCoordCalc(centerPoint.getY(),minorAxis),mayorAxis,minorAxis);
        }
    }

    public void setRectangleShadow(Point topLeft, Point bottomRight){
        if(!figureProperties.getShadowType().equals(ShadowType.NONE)){
            Rectangle shadow = new Rectangle(topLeft, bottomRight);
            gc.setFill(figureProperties.getShadowType().getColor(figureProperties.getColor()));

            gc.fillRect(figureProperties.getShadowType().shadowCoordCalc(topLeft.getX(),0),figureProperties.getShadowType().shadowCoordCalc(topLeft.getY(),0),
                    figureProperties.getShadowType().rectCalc(topLeft.getX(), bottomRight.getX()), figureProperties.getShadowType().rectCalc(topLeft.getY(), bottomRight.getY()));
        }
    }

    public void setRectangleGradient(){
        LinearGradient linearGradient = new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE,
                new Stop(0,figureProperties.getColor()), new Stop(1, figureProperties.getSecondaryColor()));
        gc.setFill(linearGradient);
    }

    public void setOvalGradient(){
        RadialGradient radialGradient = new RadialGradient(0,0,0.5,0.5,0.5,true,
                CycleMethod.NO_CYCLE,new Stop(0,figureProperties.getColor()), new Stop(1,figureProperties.getSecondaryColor()));
        gc.setFill(radialGradient);
    }

    public void setEdge(){
        gc.setLineWidth(figureProperties.getWidth());
        //gc.setLineDashes(0);
        Double[] arr = figureProperties.getEdge().getBorder();
        //gc.setLineDashes(10.0,0.0,0.0,0.0,0.0);
        gc.setLineDashes(arr[0],arr[1],arr[2],arr[3],arr[4]);
        //figureProperties.getEdge().getBorder(gc);
    }

    public abstract void drawFigure();

    public abstract DrawFigure createDrawfigure(GraphicsContext gc, FigureProperties fProperties, Figure figure);

    public boolean areEqual(){
        return figureProperties.getColor().equals(figureProperties.getSecondaryColor());
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public Figure getFigure(){
        return figure;
    }

    public FigureProperties getFigureProperties(){
        return figureProperties;
    }
}
