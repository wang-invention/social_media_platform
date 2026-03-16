package com.user.controller;

import com.user.domain.User;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        User user = userService.login(username, password);
        
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        User existingUser = userService.getUserById(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        if (existingUser != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        User registeredUser = userService.register(user);
        result.put("success", true);
        result.put("message", "注册成功");
        result.put("user", registeredUser);
        return result;
    }

    @PostMapping("/heartbeat")
    public Map<String, Object> heartbeat(@RequestBody Map<String, Integer> data) {
        Integer userId = data.get("userId");
        
        Map<String, Object> result = new HashMap<>();
        if (userId != null) {
            userService.updateHeartbeat(userId);
            result.put("success", true);
            result.put("message", "心跳更新成功");
        } else {
            result.put("success", false);
            result.put("message", "用户ID不能为空");
        }
        return result;
    }
}
