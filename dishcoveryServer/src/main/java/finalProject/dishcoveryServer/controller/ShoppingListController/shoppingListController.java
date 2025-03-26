package finalProject.dishcoveryServer.controller.ShoppingListController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import finalProject.dishcoveryServer.models.ShoppingList;
import finalProject.dishcoveryServer.service.ShoppingListService.ShoppingListService;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class shoppingListController {
    
    @Autowired
    private JwtService jwtSvc;

    @Autowired
    private ShoppingListService shoppingListSvc;

    @PostMapping("/saveShoppingList")
    public ResponseEntity<String> saveShoppingList(@RequestBody ShoppingList shopingList, HttpServletRequest request){
        
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);
        
        System.out.println("Your payload here is " + shopingList.toString());
        System.out.println("Your username " + username);

        this.shoppingListSvc.saveShoppingList(username, shopingList);

        JsonObject success = Json.createObjectBuilder()
            .add("message","successfully saved")
            .build();

        return ResponseEntity.ok().body(success.toString());
    }


    @GetMapping("/fetchShoppingList")
    public ResponseEntity<String> fetchShoppingList(HttpServletRequest request){
        
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);

        List<JsonObject> shopList = this.shoppingListSvc.fetchShoppingList(username);

        return ResponseEntity.ok().body(shopList.toString());

    }

    @PostMapping("/deleteShoppingList")
    public ResponseEntity<String> deleteShoppingList(@RequestBody String shoppingListId){

        shoppingListSvc.deleteShoppingList(shoppingListId);

        JsonObject success = Json.createObjectBuilder()
            .add("message","successfully saved")
            .build();

        return ResponseEntity.ok().body(success.toString());
        
    }
}
