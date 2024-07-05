package frontend.Exceptions;

public class HiddenLayerException extends Exception{
    private final static String MSG = "La capa esta oculta";

    public HiddenLayerException(){
        super(MSG);
    }
}
