package com.huayou.samlboot2.config;

import java.util.Arrays;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.LazyCsrfTokenRepository;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  DaoAuthenticationProvider usernamePasswordProvider;
  @Autowired
  SAMLAuthenticationProvider samlAuthenticationProvider;
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(usernamePasswordProvider)
        .authenticationProvider(samlAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    securityContextRepository.setSpringSecurityContextKey("SPRING_SECURITY_CONTEXT_SAML");
    http
        .securityContext()
        .securityContextRepository(securityContextRepository);
    http.authorizeRequests()
        .antMatchers("/home", "/", "index", "/login", "/login-error", "/401", "/css/**", "/js/**",
            "/error", "/saml/**", "/idpselection", "/ssologin", "/sso-logout").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login").defaultSuccessUrl("/home").failureUrl("/login-error")
        .and()
        .exceptionHandling().accessDeniedPage("/401").authenticationEntryPoint(samlEntryPoint);;
    http.logout().logoutSuccessUrl("/");
    http.csrf();
    http.csrf().ignoringAntMatchers("/ssologin");
    http.addFilterAfter(metadataGeneratorFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(samlWebSSOProcessingFilter, MetadataGeneratorFilter.class)
        .addFilterAfter(samlWebSSOHoKProcessingFilter, SAMLProcessingFilter.class)
        .authenticationProvider(samlAuthenticationProvider);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/ssologin");
  }

  @Bean
  public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new CompositeSessionAuthenticationStrategy(Arrays.asList(
        new ChangeSessionIdAuthenticationStrategy(),
        new CsrfAuthenticationStrategy(csrfTokenRepository())
    ));
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    return new LazyCsrfTokenRepository(new HttpSessionCsrfTokenRepository());
  }


  @Autowired
  private SAMLEntryPoint samlEntryPoint;
  @Resource(name = "samlWebSSOProcessingFilter")
  private SAMLProcessingFilter samlWebSSOProcessingFilter;
  @Resource(name = "samlWebSSOHoKProcessingFilter")
  private SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter;
  @Autowired
  private MetadataGeneratorFilter metadataGeneratorFilter;
}
