package finalProject.dishcoveryServer.controller.GroceryController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import finalProject.dishcoveryServer.models.FCMToken;
import finalProject.dishcoveryServer.service.NotificationService.NotificationService;



@RestController
@RequestMapping("/")
public class notificationController {
    
     @Autowired
    private NotificationService notificationService;

    // @PostMapping("/token")
    // public ResponseEntity<String> saveToken(@RequestBody FCMToken fcmToken) {
    //     notificationService.saveToken(fcmToken.getUsername(), fcmToken.getFcmToken());
    //     return ResponseEntity.ok("Token saved successfully");
    // }

    // @PostMapping("/send")
    // public ResponseEntity<String> sendNotification(@RequestParam String username, 
    //                                               @RequestParam String title, 
    //                                               @RequestParam String body) {
    //     notificationService.sendNotification(username, title, body);
    //     return ResponseEntity.ok("Notification sent successfully");
    // }
    
    // @GetMapping("/test")
    // public ResponseEntity<String> testNotification(@RequestParam String username) {
    //     notificationService.sendNotification(username, 
    //                                        "Test Notification", 
    //                                        "This is a test notification from Dishcovery!");
    //     return ResponseEntity.ok("Test notification sent successfully");
    // }
    
    @GetMapping("/test-direct")
    public ResponseEntity<String> testDirectNotification(@RequestParam String token) {
        notificationService.sendNotificationToToken(token, 
                                                  "Direct Test Notification", 
                                                  "This is a direct test notification from Dishcovery!");
        return ResponseEntity.ok("Direct test notification sent successfully");
    }
}
