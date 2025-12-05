package com.seatguard.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${fcm.config-path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                return;
            }
            InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Connection Established");

        } catch (Exception e) {
            e.printStackTrace();
            // 키 파일 없으면 서버 죽는 게 낫다...
            throw new RuntimeException("Firebase 초기화 실패. JSON 파일 위치 확인해라.");
        }
    }
}
