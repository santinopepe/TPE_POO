
package frontend.Exceptions;

public class NoLayerException extends Exception{
    private final static String MSG = "La capa no existe";

    public NoLayerException(){
        super(MSG);
    }
}
