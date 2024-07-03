package backend;

import backend.model.Figure;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


//Cambiamos CanvasState por layer.
public class Layer extends ArrayList<Figure> {
    private final int layerNum;
    private boolean isHidden = false;
    private boolean canEliminate = true;

    public Layer(int layerNum) {
        this.layerNum = layerNum;
    }

    public void hide(){
        isHidden = true;
    }

    public void unHide(){
        isHidden = false;
    }

    public void cannotEliminate(){
        canEliminate = false;
    }

    public boolean getEliminate(){
        return canEliminate;
    }

    public Iterable<Figure> figures() {
        return this;
    }

    public int getLayerNum(){
        return layerNum;
    }

    public boolean getIsHidden(){
        return isHidden;
    }
    @Override
    public String toString() {
        return "Capa %d".formatted(layerNum+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layer)) return false;
        return layerNum == ((Layer) o).layerNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerNum);
    }


}
