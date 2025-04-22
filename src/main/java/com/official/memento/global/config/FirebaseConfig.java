package com.official.memento.global.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config-path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount;

            if (firebaseConfigPath.startsWith("classpath:")) {
                String path = firebaseConfigPath.replace("classpath:", "");
                serviceAccount = getClass().getClassLoader().getResourceAsStream(path);
                if (serviceAccount == null) {
                    throw new FileNotFoundException("Classpath 리소스를 찾을 수 없습니다: " + path);
                }
            } else {
                serviceAccount = new FileInputStream(firebaseConfigPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}


