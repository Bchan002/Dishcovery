package finalProject.dishcoveryServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.models.User;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/auth", produces= MediaType.APPLICATION_JSON_VALUE)
public class loginRestController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtSvc; 

    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody User userLoginDetails) {
       
        //System.out.println("Your request has been received");
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLoginDetails.getUsername(), userLoginDetails.getPassword())
        );

        if(auth.isAuthenticated()){
            String token =  jwtSvc.generateToken(userLoginDetails.getUsername());

            JsonObject tokenObject = Json.createObjectBuilder()
                .add("token",token)
                .build();

            return ResponseEntity.ok().body(tokenObject.toString());

        } else {
            
            throw new UsernameNotFoundException("Invalid Username or Password!");
        }

       
    }
}
