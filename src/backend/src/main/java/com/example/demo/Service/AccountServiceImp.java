package com.example.demo.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repositories.AppRoleRepo;
import com.example.demo.Repositories.AppUserRepo;
import com.example.demo.entites.AppRole;
import com.example.demo.entites.AppUser;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImp implements AccountService {

    private final AppUserRepo appUserRepository;
    private final AppRoleRepo appRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(AppUser user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return appUserRepository.save(user);
    }

    @Override
    public AppRole addNewRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser user = appUserRepository.findByUsername(username);
        AppRole role = appRoleRepository.findByRoleName(roleName);
        user.getAppRoles().add(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }
    
}
