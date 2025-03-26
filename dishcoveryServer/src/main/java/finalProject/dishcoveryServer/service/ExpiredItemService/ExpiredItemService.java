package finalProject.dishcoveryServer.service.ExpiredItemService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import finalProject.dishcoveryServer.models.ExpiredItems;
import finalProject.dishcoveryServer.models.GroceryItem.GroceryItem;
import finalProject.dishcoveryServer.models.exception.GroceryNotSavedException;
import finalProject.dishcoveryServer.repository.ExpiredItemsRepository;
import finalProject.dishcoveryServer.repository.userRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class ExpiredItemService {
    
    @Autowired
    private ExpiredItemsRepository expItemRepo;

    @Autowired
    private userRepository userRepo;
    /*
     * GET THE EXPIRED ITEMS 
     */
    public List<JsonObject> getExpiredItems(String username){
        
        String userId = userRepo.getUserId(username);

        // Get the expired items from expired items table
        Optional<List<ExpiredItems>> expiredItems = expItemRepo.getExpiredItems(userId);

        // Check whether is null

        List<ExpiredItems> items = expiredItems.get();

        return convertExpiredItemsToJson(items);

        
    }


    public List<JsonObject> convertExpiredItemsToJson(List<ExpiredItems> items){

         try {

            List<JsonObject> expiredItemJsonList = items.stream()
                    .map(a -> {

                        JsonObject expiredObject = Json.createObjectBuilder()
                                .add("expiredItem", a.getExpiredItem())
                                .add("expiredDate", String.valueOf(a.getExpirationDate()))
                                .build();

                        return expiredObject;
                    })
                    .collect(Collectors.toList());

            return expiredItemJsonList;

        } catch (Exception e) {

            e.printStackTrace();
            throw new GroceryNotSavedException("Cannot convert GroceryList to Json List");
        }

    }


    public void clearExpiredItems(String username){
        String userId = userRepo.getUserId(username);

        this.expItemRepo.clearExpiredItems(userId);
    }

}

