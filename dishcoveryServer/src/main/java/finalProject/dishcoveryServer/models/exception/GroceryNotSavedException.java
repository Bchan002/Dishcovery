package finalProject.dishcoveryServer.models.exception;

public class GroceryNotSavedException extends RuntimeException {
    
    public GroceryNotSavedException(){

        super();
    }

    public GroceryNotSavedException(String message){

        super(message);
    }

    public GroceryNotSavedException(String message, Throwable cause){

        super(message, cause);
    }
}
