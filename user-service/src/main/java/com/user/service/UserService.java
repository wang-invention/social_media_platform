package com.user.service;

import com.user.domain.User;
import java.util.List;

public interface UserService {
    List<User> getUserList();
    User getUserById(Integer userId);
}
