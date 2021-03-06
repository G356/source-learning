package com.springboot.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class Role implements GrantedAuthority {

  private Long id;
  private String name;

  @Override
  public String getAuthority() {
    return name;
  }

}
