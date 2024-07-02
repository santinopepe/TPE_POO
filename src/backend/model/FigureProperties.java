package backend.model;
import backend.EdgeType;
import javafx.scene.paint.Color;
import backend.ShadowType;

public class FigureProperties {

    private  Color color;
    private  Color secondaryColor;
    private ShadowType shadowType;
    private  EdgeType edge;
    private Double width;

    public FigureProperties(Color color, ShadowType shadowType, Color secondaryColor, EdgeType edge, Double width) {
        this.color = color;
        this.shadowType = shadowType;
        this.secondaryColor = secondaryColor;
        this.edge = edge;
        this.width = width;
    }

    public Double getWidth() {
        return width;
    }

    public Color getColor() {
        return color;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public ShadowType getShadowType() {
        return shadowType;
    }

    public EdgeType getEdge() {
        return edge;
    }
}
