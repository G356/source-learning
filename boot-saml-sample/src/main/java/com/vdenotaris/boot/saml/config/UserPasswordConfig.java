package com.vdenotaris.boot.saml.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class UserPasswordConfig {
  @Autowired
  IDPASSDetailsService IDPASSDetailsService;
  @Bean
  public DaoAuthenticationProvider usernamePasswordProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(IDPASSDetailsService);
    return provider;
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  public ObjectMapper objectMapper(){return new ObjectMapper();}
}