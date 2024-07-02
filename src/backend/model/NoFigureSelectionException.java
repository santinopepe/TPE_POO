package backend.model;

public class NoFigureSelectionException extends RuntimeException{
    private static final String MSG = "No se selecciono ninguna figura";

    public NoFigureSelectionException() {
        super(MSG);
    }
}
