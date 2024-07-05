package backend.Exceptions;

public class NotDeletableLayerException extends RuntimeException{
    private static final String MSG = "No se puede eliminar la capa";

    public NotDeletableLayerException(){
        super(MSG);
    }
}
