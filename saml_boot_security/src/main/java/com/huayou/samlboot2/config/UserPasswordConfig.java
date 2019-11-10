package com.huayou.samlboot2.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huayou.samlboot2.spring.security.IDPASSDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
@AutoConfigureBefore(LoginSecurityConfig.class)
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

//  @Bean(value = "userPasswordSuccessHandler")
//  public AuthenticationSuccessHandler loginSuccessHandler() {
//    return(request,response,authentication)->{
//      response.setCharacterEncoding("utf-8");
//      response.setContentType("text/javascript;charset=utf-8");
//      response.getWriter().print("登录成功!");
//    };
//  }

  @Bean
  public AuthenticationFailureHandler loginFailureHandler() {
    return(request,response,exception)-> {
      String msg = null;
      if (exception instanceof BadCredentialsException) {
        msg = "密码错误";
      } else {
        msg = exception.getMessage();
      }
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/javascript;charset=utf-8");
      response.getWriter().print(msg);
    };
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request,response,authException)-> {
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/javascript;charset=utf-8");
      response.getWriter().print("未登录："+authException.getCause());
    };
  }
  @Bean
  public AccessDeniedHandler accessDeniedHandler(){
    return (request,response,accessDeniedException)->{
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/javascript;charset=utf-8");
      response.getWriter().print("没有权限:"+accessDeniedException.getMessage());
    };
  }

  @Bean(value = "userPasswordLogoutSussHandler")
  public LogoutSuccessHandler logoutSussHandler() {
    return(request,response,authentication)-> {
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/javascript;charset=utf-8");
      response.getWriter().print("退出成功!");
    };

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  public ObjectMapper objectMapper(){return new ObjectMapper();}
}
