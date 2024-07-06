package backend;


import backend.Exceptions.HiddenLayerException;
import backend.Exceptions.NotDeletableLayerException;
import backend.model.Figure;
import java.util.ArrayList;
import java.util.Objects;


//Es una capa del programa que extiende a un ArrayList de las figuras que pertencen a la capa
public class Layers extends ArrayList<Figure> {

    //LayerNum corresponde al numero de capa
    private final int layerNum;

    //Verdadero si la capa está oculta
    private boolean isHidden = false;

    //Verdadero si se puede eliminar la capa
    private boolean canEliminate = true;

    public Layers(int layerNum) {
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


    //Arroja una excepcion con la capa no puede eliminarse.
    public void canEliminateException(){
        if(!canEliminate){
            throw new NotDeletableLayerException();
        }
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

    public void getHiddenException(){
        if(isHidden){
            throw new HiddenLayerException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layers)) return false;
        return layerNum == ((Layers) o).layerNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerNum);
    }

    @Override
    public String toString() {
        return "Capa %d".formatted(layerNum+1);
    }


}

