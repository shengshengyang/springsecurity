package com.example.springsecurity.service;

import com.example.springsecurity.model.User;
import com.example.springsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updatePassword(String userName, String newPassword) {
        // 查找用户
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            // 更新密码
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }
}
