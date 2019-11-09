package com.springboot.security.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.dto.ResponseInfo;
import com.springboot.security.dto.ResponseUtil;
import com.springboot.security.service.UsernamePasswordDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class UserPasswordConfig {

  @Autowired
  UsernamePasswordDetailsService usernamePasswordDetailsService;

  @Bean
  public DaoAuthenticationProvider usernamePasswordProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(usernamePasswordDetailsService);
    return provider;
  }

  @Bean
  public AuthenticationSuccessHandler loginSuccessHandler() {
    return (request, response, authentication) -> {
      ResponseUtil.responseJson(response, HttpStatus.OK.value(), "login successful !");
    };
  }

  @Bean
  public AuthenticationFailureHandler loginFailureHandler() {
    return (request, response, exception) -> {
      String msg = null;
      if (exception instanceof BadCredentialsException) {
        msg = "wrong password !";
      } else {
        msg = exception.getMessage();
      }
      ResponseInfo info = new ResponseInfo(HttpStatus.UNAUTHORIZED.value() + "", msg);
      ResponseUtil.responseJson(response, HttpStatus.OK.value(), info);
    };
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      ResponseUtil.responseJson(response, HttpStatus.OK.value(), "Not logged in!");
    };
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      ResponseUtil.responseJson(response, HttpStatus.OK.value(), "Permission denied:" + accessDeniedException.getMessage());
    };
  }

  @Bean
  public LogoutSuccessHandler logoutSussHandler() {
    return (request, response, authentication) -> {
      ResponseUtil.responseJson(response, HttpStatus.OK.value(), "exit successfully!");
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  public ObjectMapper objectMapper(){return new ObjectMapper();}
}
