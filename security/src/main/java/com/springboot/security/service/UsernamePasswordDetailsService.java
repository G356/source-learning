package com.springboot.security.service;

import com.springboot.security.dto.Role;
import com.springboot.security.dto.User;
import com.springboot.security.mapper.UserMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordDetailsService implements UserDetailsService {

  @Resource
  private UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    User user = userMapper.loadUserByUsername(userName);
    if(user == null){
      throw new UsernameNotFoundException("Username does not exist");
    }
    user.setAuthorities(userMapper.getRolesByUserId(user.getId()));
    return user;
  }


}
