package finalProject.dishcoveryServer.controller.GroceryController;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.models.GroceryItem.GroceryItem;
import finalProject.dishcoveryServer.service.GroceryService.GroceryService;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class groceryController {

     @Autowired
    private JwtService jwtSvc;

    @Autowired
    private GroceryService grocerySvc;

    @PostMapping("/saveGrocery")
    public ResponseEntity<String> saveGrocery(@RequestBody GroceryItem[] groceryItem, HttpServletRequest request, 
    @RequestHeader("FCM-Token") String fcmToken) {

        System.out.println("recieved ur groceryItem" + groceryItem[0].getItemName());

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);
        System.out.println("Your username is here " + username);

        System.out.println("Your FCM-TOKEN is here " + fcmToken);

        // Send to grocery service 
        grocerySvc.saveGrocery(username, fcmToken, groceryItem);

        JsonObject successful = Json.createObjectBuilder()
                .add("Message: ", "Saved Grocery Successful")
                .add("TimeStamp", new Date().toString())
                .build();

        return ResponseEntity.ok().body(successful.toString());
    }


    @GetMapping("/fetchGrocery")
    public ResponseEntity<String> fetchSavedGrocery(HttpServletRequest request){
        
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);
        System.out.println("Your username is here " + username);

        //Send to service 
        List<JsonObject> savedGroceryList = grocerySvc.fetchSavedGrocery(username);

        return ResponseEntity.ok().body(savedGroceryList.toString());

    }

    @PostMapping("/deleteGrocery")
    public ResponseEntity<String> deleteGroceryById(@RequestBody String itemId){

        //Send to service
        grocerySvc.deleteGroceryById(itemId);

        JsonObject successful = Json.createObjectBuilder()
            .add("Message: ", "Delete Grocery Successful")
            .add("TimeStamp", new Date().toString())
            .build();

        return ResponseEntity.ok().body(successful.toString());
    }

    

}
