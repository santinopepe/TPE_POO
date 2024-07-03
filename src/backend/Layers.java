package backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Layers{

    private static final int FIRST_LAYER = 1;
    private static int  LAYER_NUM = FIRST_LAYER;

    private final List<Layer> layers = new ArrayList<>();

    public List<Layer> getLayers(){
        return layers;
    }

    public int getLayerNum(){
        return LAYER_NUM;
    }

    public void addLayer(){
        layers.add(new Layer(LAYER_NUM++));
    }

}

