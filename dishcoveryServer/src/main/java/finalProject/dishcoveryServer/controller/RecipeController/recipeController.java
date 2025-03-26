package finalProject.dishcoveryServer.controller.RecipeController;

import java.net.http.HttpRequest;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.models.Recipe.Recipe;
import finalProject.dishcoveryServer.service.RecipeService.RecipeService;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="/")
@CrossOrigin(origins="*")
public class recipeController {
    
    @Autowired
    private RecipeService recipeSvc;

    @Autowired
    private JwtService jwtSvc;


    @GetMapping("/recipe/popular")
    public ResponseEntity<String> getPopularRecipe(){

        
        List<JsonObject> recipeJson = recipeSvc.getPopularRecipe();


        return ResponseEntity.ok().body(recipeJson.toString());

    }

    @GetMapping("/recipe/search")
    public ResponseEntity<String> getSearchRecipe(@RequestParam String query){

        System.out.println("Your query is " + query);
        List<JsonObject> searchRecipe = recipeSvc.searchRecipe(query);

        return ResponseEntity.ok().body(searchRecipe.toString());
    }

    @PostMapping(path="/recipe/save")
    @CrossOrigin(origins="*")
    public ResponseEntity<String> saveRecipe(@RequestBody Recipe recipe, HttpServletRequest request){
        
        System.out.println("Your paylaod is here " + recipe.getRecipeId());
        
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);
        System.out.println("Your username is here " + username);

        try {
            recipeSvc.saveRecipe(recipe, username);

            JsonObject successful = Json.createObjectBuilder()
                  .add("Message: ", "Saved Successful")
                  .add("TimeStamp", new Date().toString())
                  .build();
  
          return ResponseEntity.ok().body(successful.toString());
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return ResponseEntity.status(500).body("failed to save");
        }
        //Send to service 
       
    }


    @GetMapping(path="/recipe/getSavedRecipe")
    public ResponseEntity<String> getSavedRecipes(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);

        //String username = "tester";

        // System.out.println("Hello");
        //Send to service 
        List<JsonObject> savedRecipe = recipeSvc.getAllSavedRecipes(username);

        return ResponseEntity.ok().body(savedRecipe.toString());

    }


    @PostMapping(path="/recipe/delete")
    public ResponseEntity<String> deleteSavedRecipes(@RequestBody String recipeId,HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtSvc.extractUsername(token);

        System.out.println("Your recipe id to delete is " + recipeId);

        recipeSvc.deleteSavedRecipes(recipeId);

        JsonObject successful = Json.createObjectBuilder()
            .add("Message: ", "Successfully Deleted")
            .add("TimeStamp", new Date().toString())
            .build();

        return ResponseEntity.ok().body(successful.toString());
    }






    /**
     *  PRE STORE SOME DATA INTO MONGO
     */
    @GetMapping("/recipe/store")
     public ResponseEntity<String> getPopularRecipe(@RequestParam String tags){

        
        recipeSvc.storePopularRecipe(tags);

        


        return ResponseEntity.ok().body("successfully Stored");

     }



    
}
