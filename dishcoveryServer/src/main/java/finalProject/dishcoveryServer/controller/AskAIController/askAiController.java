package finalProject.dishcoveryServer.controller.AskAIController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.service.AskAIService.askAIService;
import jakarta.json.JsonArray;

@RestController
@RequestMapping("/")
public class askAiController {
    
    @Autowired
    private askAIService asAiSvc;


    @PostMapping("/getSuggestedRecipes")
    public ResponseEntity<String> getSuggestedRecipes(@RequestBody List<String> ingredients){
        
        System.out.println("This is your ingredient " + ingredients.toString());

        //Send to AI Service 
       JsonArray result = this.asAiSvc.getSuggestedRecipes(ingredients);

       //String images = this.asAiSvc.generateRecipeImages("Mashed Potato Stuffed Chicken Breast");

        return ResponseEntity.ok().body(result.toString());
    }

    
}
