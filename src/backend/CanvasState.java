package backend;

//Esta clase se utiliza como central de capas.
public class CanvasState {
    private static final int  INITIAL_LAYER = 0;
    private static int currentLayer = INITIAL_LAYER;

    public int getAndIncrementLayer(){
        return currentLayer++;
    }

    public  int getCurrentLayer() {
        return currentLayer;
    }

}
