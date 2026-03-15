package com.chat.mapper;

import com.chat.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectUserById(@Param("userId") Integer userId);
}
