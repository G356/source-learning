package com.springboot.security;

import com.springboot.security.dto.User;
import com.springboot.security.mapper.UserMapper;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityApplicationTests {

  @Resource
  UserMapper userMapper;

  @Test
  void contextLoads() {
  }

  @Test
  void update() {
    User user = new User();
    user.setUsername("user");
    String password = "123456";
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashPass = passwordEncoder.encode(password);
    System.out.println("密码一致" + passwordEncoder.matches(password, hashPass));
    user.setPassword(hashPass);
    userMapper.update(user);
  }
}
