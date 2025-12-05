package com.seatguard.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                return;
            }

            String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
            InputStream serviceAccount;

            if (firebaseCredentials != null && !firebaseCredentials.isEmpty()) {
                serviceAccount = new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));
            } else {
                serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");
            }

            if (serviceAccount == null) {
                System.out.println("Firebase 설정 없음 - FCM 비활성화");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Connection Established");

        } catch (Exception e) {
            System.out.println("Firebase 초기화 실패 - FCM 비활성화: " + e.getMessage());
        }
    }
}
