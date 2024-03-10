package com.example.springsecurity.repo;

import com.example.springsecurity.model.FunctionPermission;
import com.example.springsecurity.model.Role;
import com.example.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FunctionPermissionRepository extends JpaRepository<FunctionPermission, Long> {

    Set<FunctionPermission> findByUser(User user);

    Set<FunctionPermission> findByRole(Role role);

}
