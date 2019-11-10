package com.huayou.samlboot2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class SecurityMvcConfig extends WebMvcConfigurationSupport {

  @Override
  protected void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/protected").setViewName("protected");
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/login-error").setViewName("login");
    registry.addViewController("/").setViewName("login");
    registry.addViewController("/user/common").setViewName("user/common");
    registry.addViewController("/admin/common").setViewName("admin/common");
    registry.addViewController("/home").setViewName("home");
  }
}