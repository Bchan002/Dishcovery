package finalProject.dishcoveryServer.controller.ExpiredItemController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.service.ExpiredItemService.ExpiredItemService;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class expiredItemController {
    
    @Autowired
    private ExpiredItemService expSvc;

    @Autowired
    private JwtService jwtSvc;

    @GetMapping("/fetchExpiredItems")
    public ResponseEntity<String> fetchExpiredItems(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);

        // String username = "tester";

        List<JsonObject> expiredItemsList = expSvc.getExpiredItems(username);

        return ResponseEntity.ok().body(expiredItemsList.toString());
    }

    @PostMapping("/clearExpiredItems")
    public ResponseEntity<String> clearExpiredItems(HttpServletRequest request) {
        
         String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);

        // String username = "tester";

        this.expSvc.clearExpiredItems(username);

        JsonObject object = Json.createObjectBuilder()
            .add("message", "successfully cleared")
            .build();

        return ResponseEntity.ok().body(object.toString());

    }

}
