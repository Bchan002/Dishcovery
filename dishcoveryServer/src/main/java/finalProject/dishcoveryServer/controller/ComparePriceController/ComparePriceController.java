package finalProject.dishcoveryServer.controller.ComparePriceController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.models.PriceCompare;
import finalProject.dishcoveryServer.service.ComparePriceService.comparePriceService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/")
public class ComparePriceController {
    
    @Autowired
    private comparePriceService comparePriceSvc;

    @PostMapping("/comparePrice")
    public ResponseEntity<String> comparePrice(@RequestBody PriceCompare priceCompare ){
        


        System.out.println("This is your request to find " + priceCompare.toString());
        //Send to service   
       //JsonArray result =this.comparePriceSvc.comparePrice(ingredients, min, max);
        
       JsonObject result = this.comparePriceSvc.priceChecker(priceCompare);

        JsonObject success = Json.createObjectBuilder()
            .add("message","success")
            .build();

        return ResponseEntity.ok().body(result.toString());
    }


}
