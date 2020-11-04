package com.revature.security.firebase;

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * @author William Gentry
 */
@Configuration
@Profile("firebase")
public class FirebaseConfig {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final FirebaseAuth firebaseAuth;

  public FirebaseConfig() {
    try {
      Resource serviceAccount = new ClassPathResource("creds.json");
      FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).build();
      this.firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(options));
      logger.info("Successfully initialized Firebase and FirebaseAuth!");
    } catch (IOException e) {
      logger.error("Failed to instantiate Firebase", e);
      throw new RuntimeException(e);
    }
  }

  @Primary
  @Bean
  @Profile("firebase")
  public AuthenticationManager firebaseAuthenticationManager() {
    return new FirebaseAuthenticationManager(this.firebaseAuth);
  }
}
