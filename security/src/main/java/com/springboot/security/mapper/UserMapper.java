package com.springboot.security.mapper;

import com.springboot.security.dto.Role;
import com.springboot.security.dto.RolePermisson;
import com.springboot.security.dto.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

  @Select("select id , username , password from user where username = #{username}")
  User loadUserByUsername(@Param("username") String username);

  @Update("update user set password = #{password} where username=#{username}")
  int update(User user);

  @Select("SELECT A.id,A.name FROM role A LEFT JOIN user_role B ON A.id=B.role_id WHERE B.user_id=${userId}")
  List<Role> getRolesByUserId(@Param("userId") Long userId);

  @Select("SELECT A.NAME AS roleName,C.url FROM role AS A LEFT JOIN role_permission B ON A.id=B.role_id LEFT JOIN permission AS C ON B.permission_id=C.id")
  List<RolePermisson> getRolePermissions();

}
