package com.unio.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayInputStream
import java.util.Base64

@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseApp(): FirebaseApp {
        if (FirebaseApp.getApps().isNotEmpty()) {
            return FirebaseApp.getInstance()
        }

        val credentials = getCredentials()
        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    private fun getCredentials(): GoogleCredentials {
        // Production: read base64-encoded service account from env var
        val envServiceAccount = System.getenv("FIREBASE_SERVICE_ACCOUNT")
        if (!envServiceAccount.isNullOrBlank()) {
            val decoded = Base64.getDecoder().decode(envServiceAccount)
            return GoogleCredentials.fromStream(ByteArrayInputStream(decoded))
        }

        // Local dev: read from classpath
        return GoogleCredentials.fromStream(
            ClassPathResource("firebase-service-account.json").inputStream
        )
    }
}
