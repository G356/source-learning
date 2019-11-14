package com.vdenotaris.boot.saml.service;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class IDPASSDetailsService implements UserDetailsService {
  private PasswordEncoder encoder=new BCryptPasswordEncoder();
  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    if(userName==null){
      throw new UsernameNotFoundException("Username does not exist");
    }
    String password = encoder.encode("123456");
    List<SimpleGrantedAuthority> auths = Collections
        .singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    System.out.println("userName : "+userName);
    System.out.println("password : "+password);
    System.out.println("auths : "+auths);
    return new User(userName,password,auths);
  }


}