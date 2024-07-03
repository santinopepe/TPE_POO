package backend;

import backend.model.Figure;
import java.util.ArrayList;
import java.util.List;


//Cambiamos CanvasState por layer.
public class Layer extends ArrayList<Figure> implements List<Figure>{
    private final int layerNum;
    private boolean isHidden = false;
    private boolean canEliminate = true;

    public Layer(int layerNum) {
        this.layerNum = layerNum-1;
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
}
