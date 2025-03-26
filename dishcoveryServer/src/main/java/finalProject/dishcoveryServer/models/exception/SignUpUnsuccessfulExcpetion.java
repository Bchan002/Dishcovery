package finalProject.dishcoveryServer.models.exception;

public class SignUpUnsuccessfulExcpetion extends RuntimeException {
    
    public SignUpUnsuccessfulExcpetion(){

        super();
    }

    public SignUpUnsuccessfulExcpetion(String message){

        super(message);
    }

    public SignUpUnsuccessfulExcpetion(String message, Throwable cause){

        super(message, cause);
    }
}
