package finalProject.dishcoveryServer.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import finalProject.dishcoveryServer.models.exception.EmailNotFoundException;
import finalProject.dishcoveryServer.models.exception.GroceryNotSavedException;
import finalProject.dishcoveryServer.models.exception.RecipeNotFoundException;
import finalProject.dishcoveryServer.models.exception.ShoppingListException;
import finalProject.dishcoveryServer.models.exception.SignUpUnsuccessfulExcpetion;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(SignUpUnsuccessfulExcpetion.class)
    public ResponseEntity<String> handleResourceNotFound(SignUpUnsuccessfulExcpetion ex, 
    HttpServletRequest request, HttpServletResponse response){

        //ApiError apiError = new ApiError(404, ex.getMessage(), new Date(), request.getRequestURI());

        JsonObject apiErrorJson = Json.createObjectBuilder() 
            .add("status", 404)
            .add("message", ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorJson.toString());
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(UsernameNotFoundException ex, 
    HttpServletRequest request, HttpServletResponse response){

        //ApiError apiError = new ApiError(404, ex.getMessage(), new Date(), request.getRequestURI());

        JsonObject apiErrorJson = Json.createObjectBuilder() 
            .add("status", 404)
            .add("message", ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorJson.toString());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex,
            HttpServletRequest request, HttpServletResponse response) {
        
        JsonObject apiErrorJson = Json.createObjectBuilder()
            .add("status", 401)
            .add("message", "Invalid username or password")
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();
            
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorJson.toString());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request, HttpServletResponse response) {
        
        JsonObject apiErrorJson = Json.createObjectBuilder()
            .add("status", 401)
            .add("message", "Authentication failed: " + ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();
            
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorJson.toString());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(EmailNotFoundException ex, 
    HttpServletRequest request, HttpServletResponse response){

        //ApiError apiError = new ApiError(404, ex.getMessage(), new Date(), request.getRequestURI());

        JsonObject apiErrorJson = Json.createObjectBuilder() 
            .add("status", 404)
            .add("message", ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorJson.toString());
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(RecipeNotFoundException ex, 
    HttpServletRequest request, HttpServletResponse response){

        //ApiError apiError = new ApiError(404, ex.getMessage(), new Date(), request.getRequestURI());

        JsonObject apiErrorJson = Json.createObjectBuilder() 
            .add("status", 404)
            .add("message", ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorJson.toString());
    }

    @ExceptionHandler(GroceryNotSavedException.class)
    public ResponseEntity<String> handleResourceNotFound(GroceryNotSavedException ex, 
    HttpServletRequest request, HttpServletResponse response){

        //ApiError apiError = new ApiError(404, ex.getMessage(), new Date(), request.getRequestURI());

        JsonObject apiErrorJson = Json.createObjectBuilder() 
            .add("status", 404)
            .add("message", ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorJson.toString());
    }

    @ExceptionHandler(ShoppingListException.class)
    public ResponseEntity<String> handleResourceNotFound(ShoppingListException ex, 
    HttpServletRequest request, HttpServletResponse response){

        //ApiError apiError = new ApiError(404, ex.getMessage(), new Date(), request.getRequestURI());

        JsonObject apiErrorJson = Json.createObjectBuilder() 
            .add("status", 404)
            .add("message", ex.getMessage())
            .add("timeStamp", new Date().toString())
            .add("path", request.getRequestURI())
            .build();


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorJson.toString());
    }

}
