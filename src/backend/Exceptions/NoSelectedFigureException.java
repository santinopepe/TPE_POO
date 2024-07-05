package backend.Exceptions;

public class NoSelectedFigureException extends Exception{
    private final static String MSG = "Ninguna figura seleccionada";

    public NoSelectedFigureException(){
        super(MSG);
    }
}
