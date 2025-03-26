package finalProject.dishcoveryServer.models.exception;

public class RecipeNotFoundException extends RuntimeException {
    
    public RecipeNotFoundException(){

        super();
    }

    public RecipeNotFoundException(String message){

        super(message);
    }

    public RecipeNotFoundException(String message, Throwable cause){

        super(message, cause);
    }
}
