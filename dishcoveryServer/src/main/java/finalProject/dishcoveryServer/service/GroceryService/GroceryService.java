package finalProject.dishcoveryServer.service.GroceryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProject.dishcoveryServer.models.FCMToken;
import finalProject.dishcoveryServer.models.GroceryItem.GroceryItem;
import finalProject.dishcoveryServer.models.exception.GroceryNotSavedException;
import finalProject.dishcoveryServer.repository.FcmRepository;
import finalProject.dishcoveryServer.repository.GroceryRepository;
import finalProject.dishcoveryServer.repository.userRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class GroceryService {

    @Autowired
    private userRepository userRepo;

    @Autowired
    private GroceryRepository groceryRepo;

    @Autowired
    private FcmRepository fcmRepo;

    @Transactional(rollbackFor = GroceryNotSavedException.class)
    public void saveGrocery(String username, String fcmToken, GroceryItem[] groceryItems) {

        // Get the userId from userDetails
        String userId = userRepo.getUserId(username);

        // Store the groceryItem
        for (GroceryItem item : groceryItems) {
            // Generate itemId
            String itemId = UUID.randomUUID().toString().substring(0, 7);
            // Create a new Grocery Object
            GroceryItem grocery = new GroceryItem();
            grocery.setItemId(itemId);
            grocery.setItemName(item.getItemName());
            grocery.setQuantity(item.getQuantity());
            grocery.setUserId(userId);
            grocery.setCategory(item.getCategory());
            grocery.setExpirationDate(item.getExpirationDate());

            // Send to Grocery Repository
            groceryRepo.saveGrocery(grocery);
        }

        // Check whether FCM token is inside the database first or not
        Optional<List<FCMToken>> storeFCMToken = fcmRepo.fetchSavedFcmToken(userId);

        // Check whether is null
        if (storeFCMToken.isPresent() && !storeFCMToken.get().isEmpty()) {
            List<FCMToken> tokens = storeFCMToken.get();
            boolean tokenExists = tokens.stream()
                    .anyMatch(token -> token.getFcmToken().equals(fcmToken));

            if (!tokenExists) {
                // Store the new FCM token
                FCMToken fcmToken2 = new FCMToken();
                fcmToken2.setFcmToken(fcmToken);
                fcmToken2.setUserId(userId);
                fcmRepo.saveFcmToken(fcmToken2);
            }
        } else {
            // No existing tokens, create first one
            FCMToken fcmToken2 = new FCMToken();
            fcmToken2.setFcmToken(fcmToken);
            fcmToken2.setUserId(userId);
            fcmRepo.saveFcmToken(fcmToken2);
        }

    }

    /*
     * FETCH SAVED GROCERY
     */
    public List<JsonObject> fetchSavedGrocery(String username) {

        // Get User id from userDetails
        String userId = userRepo.getUserId(username);

        // Get the saved Grocery from groceryItemstable
        Optional<List<GroceryItem>> groceryItems = groceryRepo.fetchSavedGrocery(userId);

        // Check whether is null

        List<GroceryItem> items = groceryItems.get();

        // Convert to list of JsonObject
        return convertGroceryListToJsonList(items);

    }

    /*
     * HELPER METHOD TO CONVERT TO JSON
     * 
     * export interface GroceryItems {
     * itemId?: string
     * userId: string
     * itemName: string
     * quantity:number
     * category: string
     * expirationDate: string
     * last_notified?: string
     * created_at?: string
     * }
     */
    public List<JsonObject> convertGroceryListToJsonList(List<GroceryItem> items) {

        try {

            List<JsonObject> groceryItemJsonList = items.stream()
                    .map(a -> {

                        JsonObject groceryObject = Json.createObjectBuilder()
                                .add("itemId", a.getItemId())
                                .add("itemName", a.getItemName())
                                .add("quantity", a.getQuantity())
                                .add("category", a.getCategory())
                                .add("expirationDate", a.getExpirationDate())
                                .build();

                        return groceryObject;
                    })
                    .collect(Collectors.toList());

            return groceryItemJsonList;

        } catch (Exception e) {

            e.printStackTrace();
            throw new GroceryNotSavedException("Cannot convert GroceryList to Json List");
        }

    }

    @Transactional(rollbackFor = GroceryNotSavedException.class)
    public void deleteGroceryById(String itemId) {

        groceryRepo.deleteGroceryById(itemId);
    }
}
