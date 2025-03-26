package finalProject.dishcoveryServer.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.models.SignUpDetails;
import finalProject.dishcoveryServer.service.userService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class signUpRestController {

    @Autowired
    private userService userSvc;

    @PostMapping("/signUp")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> postSignUpDetails(@RequestBody SignUpDetails signUpDetails) {

        System.out.println("Signing up...");
        userSvc.postSignUpDetails(signUpDetails);

        JsonObject successful = Json.createObjectBuilder()
                .add("Message: ", "Sign Up Successful")
                .add("TimeStamp", new Date().toString())
                .build();

        return ResponseEntity.ok().body(successful.toString());

    }

}
