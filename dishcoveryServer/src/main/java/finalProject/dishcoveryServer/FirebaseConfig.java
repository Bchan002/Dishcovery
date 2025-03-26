package finalProject.dishcoveryServer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.Resource;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config}")
    private String firebaseJson;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        // // // Load the service account key JSON file
        // GoogleCredentials googleCredentials = GoogleCredentials
        //         .fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());

        // FirebaseOptions options = FirebaseOptions.builder()
        //         .setCredentials(googleCredentials)
        //         .build();

        //Deployment
        if (firebaseJson == null || firebaseJson.isEmpty()) {
            throw new IllegalStateException("firebase.config property is missing");
        }

        InputStream serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // Initialize the app if not already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
