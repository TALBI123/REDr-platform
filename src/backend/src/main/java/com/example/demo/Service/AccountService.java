package com.example.demo.Service;

import java.util.List;

import com.example.demo.entites.AppRole;
import com.example.demo.entites.AppUser;

public interface AccountService {
    AppUser addNewUser(AppUser user); 
    AppRole addNewRole(AppRole role);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
} 