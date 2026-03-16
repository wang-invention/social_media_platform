package com.user.task;

import com.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserOfflineTask {

    @Autowired
    private UserMapper userMapper;

    @Scheduled(fixedRate = 30000)
    public void checkOfflineUsers() {
        userMapper.updateOfflineUsers();
    }
}
