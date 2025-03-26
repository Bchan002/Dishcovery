package finalProject.dishcoveryServer.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import finalProject.dishcoveryServer.models.ExpiredItems;
import finalProject.dishcoveryServer.models.exception.GroceryNotSavedException;
import finalProject.dishcoveryServer.models.exception.ShoppingListException;
import finalProject.dishcoveryServer.utils.expiredSql;

@Repository
public class ExpiredItemsRepository {
    
    @Autowired
    private JdbcTemplate template;


    //Store the expired items in mysql 
    public void saveExpiredItems(String userId, String expiredItems, LocalDate expiredDate){

         try {
            int saved = template.update(expiredSql.SAVE_EXPIRED_ITEMS, userId,expiredItems,expiredDate);
    
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShoppingListException("Cannot save expired items");
        }
    
    }

    // Get the expired items in mysql 
    public Optional<List<ExpiredItems>> getExpiredItems(String userId){

            List<ExpiredItems> expiredItemsList = new ArrayList<>();

        try {
            
            expiredItemsList = template.query(expiredSql.GET_EXPIRED_ITEMS,
            BeanPropertyRowMapper.newInstance(ExpiredItems.class), userId);

            return Optional.of(expiredItemsList);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

            throw new ShoppingListException("Cannot fetch expired items from mysql");
        }
    }


    // Clear the notifications 
    public void clearExpiredItems(String userId){

        try {
            int cleared = template.update(expiredSql.CLEAR_EXPIRED_ITEMS, userId);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new ShoppingListException("Cannot clear expired itsm from mysql");
        }
    }
}
