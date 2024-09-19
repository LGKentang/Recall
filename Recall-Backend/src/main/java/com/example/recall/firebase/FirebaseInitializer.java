package com.example.recall.firebase;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Component
public class FirebaseInitializer {

    public static void init() {

        try {
            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://recall-fae14-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { 
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully.");
            } else {
                System.out.println("FirebaseApp already initialized.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing Firebase");
        }
    }
}
