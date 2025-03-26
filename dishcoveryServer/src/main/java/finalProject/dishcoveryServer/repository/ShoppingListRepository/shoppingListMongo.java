package finalProject.dishcoveryServer.repository.ShoppingListRepository;

import static finalProject.dishcoveryServer.utils.Constants.C_SAVE_SHOPPING_LIST;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import finalProject.dishcoveryServer.models.exception.ShoppingListException;

@Repository
public class shoppingListMongo {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    /*
     *  SAVE SHOPPING LIST IN MONGO
     */
    public void saveShoppingListMongo(Document doc){
        
        try {
            Criteria criteria = Criteria.where("_id").is(doc.get("_id"));

            Query query = Query.query(criteria);
    
            Update updateOps = new Update()
                    .set("recipeName", doc.get("recipeName"))
                    .set("recpieImage", doc.get("recipeImage"))
                    .set("ingredients",doc.get("ingredients"));
    
            UpdateResult updateResult = mongoTemplate.upsert(query, updateOps, C_SAVE_SHOPPING_LIST);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            throw new ShoppingListException("Cannot save Shopping List to Mongo");
        }
   
    }


    /*
     * FETCH SHOPPING LIST IN MONGO
     */
    public Document fetchShoppingList(String shopListId){

        try {

            Criteria criteria = Criteria.where("_id").is(shopListId);

            Query query = Query.query(criteria);
    
            Document savedRecipe = mongoTemplate.findOne(query, Document.class, C_SAVE_SHOPPING_LIST);
    
            return savedRecipe;
            
        } catch (Exception e) {
            
            e.printStackTrace();
            throw new ShoppingListException("Cannot fetch the shopping list document from Mongo");
        }

    }

    /*
     *  DELETE SHOPPING LIST IN MONGO 
     * 
     */

    public void deleteShoppingList(String shoppingListId) {

        Criteria criteria = Criteria.where("_id").is(shoppingListId);

        Query query = Query.query(criteria)
                .limit(1);

        DeleteResult result = mongoTemplate.remove(query, C_SAVE_SHOPPING_LIST);

        // Get the document count
        int count = (int) result.getDeletedCount();


    }

}
