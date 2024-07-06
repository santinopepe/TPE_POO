package backend.Exceptions;

//Salta una excepción si se quiere dibujar figuras en una capa oculta
public class HiddenLayerException extends RuntimeException{
    private final static String MSG = "La capa esta oculta";

    public HiddenLayerException(){
        super(MSG);
    }
}
