package backend.Exceptions;

//Salta una excepción se quiere eliminar una capa no eliminable
public class NotDeletableLayerException extends RuntimeException{
    private static final String MSG = "No se puede eliminar la capa";

    public NotDeletableLayerException(){
        super(MSG);
    }
}
