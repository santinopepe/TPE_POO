package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;

public class CanvasState extends  ArrayList<Figure> implements List<Figure>{

    public Iterable<Figure> figures() {
        return this;
    }

}
