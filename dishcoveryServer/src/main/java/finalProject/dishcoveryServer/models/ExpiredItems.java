package finalProject.dishcoveryServer.models;

import java.time.LocalDate;

public class ExpiredItems {
    
    private int expiredItemId;
    private String userId;
    private String expiredItem;
    private LocalDate expirationDate;
    
    public int getExpiredItemId() {
        return expiredItemId;
    }
    public void setExpiredItemId(int expiredItemId) {
        this.expiredItemId = expiredItemId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getExpiredItem() {
        return expiredItem;
    }
    public void setExpiredItem(String expiredItem) {
        this.expiredItem = expiredItem;
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    
}
