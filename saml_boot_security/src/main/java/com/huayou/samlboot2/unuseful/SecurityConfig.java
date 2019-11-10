//package com.huayou.samlboot2.config;
//
//import java.util.Arrays;
//import javax.annotation.Resource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
//import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//import org.springframework.security.web.csrf.LazyCsrfTokenRepository;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//  @Autowired
//  DaoAuthenticationProvider usernamePasswordProvider;
//  @Resource(name = "userPasswordSuccessHandler")
//  private AuthenticationSuccessHandler userPasswordSuccessHandler;
//  @Autowired
//  private AuthenticationFailureHandler authenticationFailureHandler;
//  @Resource(name = "userPasswordLogoutSussHandler")
//  private LogoutSuccessHandler userPasswordLogoutSussHandler;
//  @Autowired
//  private AuthenticationEntryPoint authenticationEntryPoint;
//  @Autowired
//  private AccessDeniedHandler accessDeniedHandler;
//  @Autowired
//  @Qualifier("authenticationManagerBean")
//  private AuthenticationManager authenticationManager;
//
//  @Autowired
//  public void configureGlobal(AuthenticationManagerBuilder auth) {
//    auth.authenticationProvider(usernamePasswordProvider);
//  }
//
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests()
//        .antMatchers("/","index","/login","/login-error","/401","/css/**","/js/**").permitAll()
//        .anyRequest().authenticated()
//        .and()
//        .formLogin().loginPage( "/login" ).failureUrl( "/login-error" )
//        .and()
//        .exceptionHandling().accessDeniedPage( "/401" );
//    http.logout().logoutSuccessUrl( "/" );
//  }
//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    super.configure(web);
//  }
//  @Bean
//  public UsernamePasswordAuthenticationFilter usernamePasswordFilter() {
//    UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
//    filter.setFilterProcessesUrl("/login");
//    filter.setAuthenticationManager(authenticationManager);
//    filter.setAuthenticationSuccessHandler(userPasswordSuccessHandler);
//    filter.setAuthenticationFailureHandler(authenticationFailureHandler);
//    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
//    return filter;
//  }
//
//  @Override
//  @Bean
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
//
//  @Bean
//  public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//    return new CompositeSessionAuthenticationStrategy(Arrays.asList(
//        new ChangeSessionIdAuthenticationStrategy(),
//        new CsrfAuthenticationStrategy(csrfTokenRepository())
//    ));
//  }
//
//  @Bean
//  public CsrfTokenRepository csrfTokenRepository() {
//    return new LazyCsrfTokenRepository(new HttpSessionCsrfTokenRepository());
//  }
//}
