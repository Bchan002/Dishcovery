package finalProject.dishcoveryServer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@CrossOrigin(origins="*")
public class testinController {

    @GetMapping("/testing")
    @CrossOrigin(origins="*")
    public ResponseEntity<String> getResponse(){
        
        String response = "You have succesfully access authroized data knn";

        return ResponseEntity.ok().body(response);
    }

}
