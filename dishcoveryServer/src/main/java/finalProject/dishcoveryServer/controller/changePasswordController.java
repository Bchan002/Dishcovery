package finalProject.dishcoveryServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.service.ChangePasswordService;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class changePasswordController {
    
    @Autowired
    private JwtService jwtSvc;

    @Autowired
    private ChangePasswordService changePasswordSvc;

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody String password, HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);

        System.out.println("This is your password " +  password);
        this.changePasswordSvc.changePassword(username, password);


        JsonObject success = Json.createObjectBuilder()
            .add("message","successfully saved")
            .build();

        return ResponseEntity.ok().body(success.toString());
    } 
}
