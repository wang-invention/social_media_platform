package com.user.mapper;

import com.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectUserList();
    User selectUserById(@Param("userId") Integer userId);
}
