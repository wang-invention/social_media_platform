package com.chat.service.impl;

import com.chat.domain.User;
import com.chat.mapper.UserMapper;
import com.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }
}
