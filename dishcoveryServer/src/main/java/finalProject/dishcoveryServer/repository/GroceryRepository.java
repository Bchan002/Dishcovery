package finalProject.dishcoveryServer.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import finalProject.dishcoveryServer.utils.grocerySql;
import finalProject.dishcoveryServer.models.User;
import finalProject.dishcoveryServer.models.GroceryItem.GroceryItem;
import finalProject.dishcoveryServer.models.GroceryItem.GroceryItemChecker;
import finalProject.dishcoveryServer.models.exception.GroceryNotSavedException;


@Repository
public class GroceryRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean saveGrocery(GroceryItem groceryItem){
        
        try {
            int saved = jdbcTemplate.update(grocerySql.INSERT_GROCERY_ITEM, 
            groceryItem.getItemId(),groceryItem.getUserId(), groceryItem.getItemName(), groceryItem.getCategory(), 
            groceryItem.getExpirationDate(),groceryItem.getQuantity());
    
            if(saved>0){
                return true;
            }
    
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new GroceryNotSavedException("Cannot save Grocery into database Error");
        }
       
    }


    public Optional<List<GroceryItem>>fetchSavedGrocery(String userId){

        List<GroceryItem> groceryItems = new ArrayList<>();

        try {
            
                groceryItems = jdbcTemplate.query(grocerySql.FETCH_SAVED_GROCERY,
                    BeanPropertyRowMapper.newInstance(GroceryItem.class), userId);
            
            return Optional.of(groceryItems);

        } catch (Exception e) {
            
            throw new GroceryNotSavedException("Cannot fetch saved grocery");
            
        }

    }

    public Boolean deleteGroceryById(String itemId){

        try {
            int deleted = jdbcTemplate.update(grocerySql.DELETE_SAVED_GROCERY, itemId);
    
            if(deleted>0){
                return true;
            }
    
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new GroceryNotSavedException("Cannot delete Grocery in database Error");
        }
    }


    /*
     *  SENDING FIREBASE NOTIFICATION 
     */

     // Fetch Expired Items that have not been notified yet less than but equal to the current date
     public Optional<List<GroceryItemChecker>> findExpiredItems() {

        List<GroceryItemChecker> groceryItemsChecker = new ArrayList<>();

        try {
            
            groceryItemsChecker = jdbcTemplate.query(grocerySql.FIND_EXPIRED_ITEMS,
                BeanPropertyRowMapper.newInstance(GroceryItemChecker.class));
        
        return Optional.of(groceryItemsChecker);

    } catch (Exception e) {
        
        throw new GroceryNotSavedException("Cannot fetch Expired Items ");
        
    }

    }

    // Update the lastNotified column after sending notification 
    public void updateLastNotification(String itemId){

        try {
            
            jdbcTemplate.update(grocerySql.UPDATE_LAST_NOTIFICATION, itemId);

            
        } catch (Exception e) {
            e.printStackTrace();
            throw new GroceryNotSavedException("Cannot Update Last Notification");
        }
            
    }

   

    

}
