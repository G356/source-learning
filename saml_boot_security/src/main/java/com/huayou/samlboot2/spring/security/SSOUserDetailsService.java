package com.huayou.samlboot2.spring.security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;
/**
 * @author
 */
@Slf4j
@Service
public class SSOUserDetailsService implements SAMLUserDetailsService {

  public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
    log.info("Login received for user {}", credential.getNameID().getValue());
    SAMLUserDetails userDetails = new SAMLUserDetails(credential);
    userDetails.setUsername(credential.getNameID().getValue());
    return userDetails;
  }
}
