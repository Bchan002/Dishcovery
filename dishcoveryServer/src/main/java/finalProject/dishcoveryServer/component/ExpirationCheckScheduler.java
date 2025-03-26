package finalProject.dishcoveryServer.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.services.storage.model.Notification;

import finalProject.dishcoveryServer.service.NotificationService.NotificationService;

@Component
public class ExpirationCheckScheduler {
    
    @Autowired
    private NotificationService notificationSvc;

    
}
