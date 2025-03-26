package finalProject.dishcoveryServer.service.ShoppingListService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProject.dishcoveryServer.models.ShoppingList;
import finalProject.dishcoveryServer.models.exception.RecipeNotFoundException;
import finalProject.dishcoveryServer.models.exception.ShoppingListException;
import finalProject.dishcoveryServer.repository.userRepository;
import finalProject.dishcoveryServer.repository.ShoppingListRepository.shoppingListMongo;
import finalProject.dishcoveryServer.repository.ShoppingListRepository.shoppingListRepoSql;
import finalProject.dishcoveryServer.utils.shoppingListSql;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Service
public class ShoppingListService {

    @Autowired
    private shoppingListMongo shopListMongoRepo;

    @Autowired
    private shoppingListRepoSql shopListSqlRepo;

    @Autowired
    private userRepository userRepo;

    /*
     * SAVE SHOPPING LIST
     */

    @Transactional(rollbackFor = ShoppingListException.class)
    public void saveShoppingList(String username, ShoppingList shopList) {

        // Get the userId from userRepo
        String userId = userRepo.getUserId(username);

        // Generate Random shopingList Id
        String shopListId = UUID.randomUUID().toString().substring(0, 8);

        // Save to mysql
        shopListSqlRepo.saveShoppingList(shopListId, userId);

        // Convert the shopList to document
        Document shopListDoc = convertObjectToDocument(shopList, shopListId);
        // Save to mongo
        shopListMongoRepo.saveShoppingListMongo(shopListDoc);

    }

    /*
     * HELPER METHOD TO CONVERT SHOPLIST OBJECT -> DOCUMENT
     */
    public Document convertObjectToDocument(ShoppingList shopList, String shopListId) {

        Document shopListDoc = new Document()
                .append("_id", shopListId)
                .append("recipeName", shopList.getRecipeName())
                .append("recipeImage", shopList.getRecipeImage())
                .append("ingredients", shopList.getIngredients());

        return shopListDoc;

    }

    /*
     * FETCH SHOPPING LIST
     */
    public List<JsonObject> fetchShoppingList(String username) {

        List<Document> shopListDocs = new ArrayList<>();

        // Get user id from repo
        String userId = userRepo.getUserId(username);

        // Get the List of Shopping Ids
        List<String> shopIdList = shopListSqlRepo.fetchShoppingList(userId);

        for (String shopId : shopIdList) {

            // Fetch the information from shopping list mongo
            Document shopListDetails = shopListMongoRepo.fetchShoppingList(shopId);

            shopListDocs.add(shopListDetails);
        }

        // Convert the Document to JsonObject
        List<JsonObject> fetchShopList = convertDocListToJsonList(shopListDocs);

        return fetchShopList;

    }

    public List<JsonObject> convertDocListToJsonList(List<Document> shopListDocs) {
        return shopListDocs.stream()
                .map(doc -> {
                    JsonArrayBuilder ingredientsArray = Json.createArrayBuilder();
                    List<String> ingredients = doc.getList("ingredients", String.class);
                    ingredients.forEach(ingredientsArray::add);

                    return Json.createObjectBuilder()
                            .add("shopListId", doc.getString("_id"))
                            .add("recipeName", doc.getString("recipeName"))
                            .add("recipeImage", doc.getString("recpieImage")) // fix: recpieImage → recipeImage
                            .add("ingredients", ingredientsArray)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ShoppingListException.class)
    public void deleteShoppingList(String shoppingListId){

         try {
            
            //Delete from mysql
            shopListSqlRepo.deleteShoppingList(shoppingListId);

            //Delete from mongo 
            shopListMongoRepo.deleteShoppingList(shoppingListId);

        } catch (Exception e) {
            // TODO: handle exception
            throw new ShoppingListException("Cannot delete the shopping List");
        }
     
    }

}
