package com.example.springsecurity.service;

import com.example.springsecurity.model.Function;
import com.example.springsecurity.model.FunctionPermission;
import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repo.FunctionPermissionRepository;
import com.example.springsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionPermissionRepository functionPermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("DEBUG: Attempting to load user - " + username);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // 獲取用戶直接對應到的function
        Set<FunctionPermission> userFunctionPermissions = functionPermissionRepository.findByUser(user);
        Set<Function> userFunctions = new HashSet<>();
        for (FunctionPermission functionPermission : userFunctionPermissions) {
            userFunctions.add(functionPermission.getFunction());
        }

        // 獲取用戶間接從role 對應到的function
        Set<Role> userRoles = user.getRoles();
        for (Role role : userRoles) {
            Set<FunctionPermission> roleFunctionPermissions = functionPermissionRepository.findByRole(role);
            for (FunctionPermission functionPermission : roleFunctionPermissions) {
                userFunctions.add(functionPermission.getFunction());
            }
        }

        // 將function 權限加入 UserDetails
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Function function : userFunctions) {
            authorities.add(new SimpleGrantedAuthority(function.getFunctionName()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
