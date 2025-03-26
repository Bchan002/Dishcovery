package finalProject.dishcoveryServer.utils;

public class grocerySql {
    
    public static final String INSERT_GROCERY_ITEM= """
        INSERT into groceryItems (itemId,userId,itemName,category,expiration_date,quantity) values (?,?,?,?,?,?)
        """;

    public static final String FETCH_SAVED_GROCERY = """
            SELECT itemId, itemName, category, expiration_date, quantity FROM groceryItems
            WHERE userId = ?;
            """;

    public static final String DELETE_SAVED_GROCERY = """
            DELETE FROM groceryItems WHERE itemId = ?
            """;

    public static final String FIND_EXPIRED_ITEMS = """
            SELECT * FROM groceryItems 
            WHERE expiration_date <= CURRENT_DATE + INTERVAL 3 DAY
            AND expiration_date >= CURRENT_DATE
            AND (last_notified IS NULL OR last_notified < CURRENT_DATE)
          
            """;

    public static final String UPDATE_LAST_NOTIFICATION = """
            UPDATE groceryItems SET last_notified = CURRENT_DATE WHERE itemId = ?
            """;
}
