package finalProject.dishcoveryServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.service.EmailService.emailService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE)
public class forgetPasswordController {
    
    @Autowired
    private emailService emailSvc;

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody String email){

        String getResponse = emailSvc.checkEmail(email);

        JsonObject objectResponse = Json.createObjectBuilder()
            .add("Message", getResponse)
            .build();

        return ResponseEntity.ok().body(objectResponse.toString());
    }
}
