package finalProject.dishcoveryServer.models.GroceryItem;

import java.time.LocalDate;

public class GroceryItemChecker {

    private String itemId;
    private String userId;
    private String itemName;
    private String category;
    private LocalDate expirationDate;
    private LocalDate lastNotified;
    
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    public LocalDate getLastNotified() {
        return lastNotified;
    }
    public void setLastNotified(LocalDate lastNotified) {
        this.lastNotified = lastNotified;
    }

    

}
