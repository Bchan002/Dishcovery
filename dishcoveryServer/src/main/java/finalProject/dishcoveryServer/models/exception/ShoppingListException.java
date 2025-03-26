package finalProject.dishcoveryServer.models.exception;

public class ShoppingListException extends RuntimeException{
    
    public ShoppingListException(){

        super();
    }

    public ShoppingListException(String message){

        super(message);
    }

    public ShoppingListException(String message, Throwable cause){

        super(message, cause);
    }
}
