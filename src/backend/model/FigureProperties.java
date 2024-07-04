package backend.model;
import backend.EdgeType;
import javafx.scene.paint.Color;
import backend.ShadowType;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FigureProperties that)) return false;
        return Objects.equals(getColor(), that.getColor()) && Objects.equals(getSecondaryColor(), that.getSecondaryColor()) && getShadowType() == that.getShadowType() && getEdge() == that.getEdge() && Objects.equals(getWidth(), that.getWidth());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setShadowType(ShadowType shadowType) {
        this.shadowType = shadowType;
    }

    public void setEdge(EdgeType edge) {
        this.edge = edge;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
}
