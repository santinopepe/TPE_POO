package backend.Exceptions;

public class HiddenLayerException extends RuntimeException{
    private final static String MSG = "La capa esta oculta";

    public HiddenLayerException(){
        super(MSG);
    }
}
