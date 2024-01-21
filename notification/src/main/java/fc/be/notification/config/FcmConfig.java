package fc.be.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FcmConfig {

    private final FcmProperties fcmProperties;

    public FcmConfig(FcmProperties fcmProperties) {
        this.fcmProperties = fcmProperties;
    }

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            FileInputStream serviceAccount = new FileInputStream(fcmProperties.getKeyPath());

            FirebaseOptions options = FirebaseOptions
                    .builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FireBase Message Load Fail");
        }
    }

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
