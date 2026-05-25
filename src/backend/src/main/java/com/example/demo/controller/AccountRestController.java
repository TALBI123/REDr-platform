package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.AccountService;
import com.example.demo.entites.AppRole;
import com.example.demo.entites.AppUser;
import com.example.demo.entites.RoleUserForm;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AccountRestController {

    private final AccountService accountService;
    
    @GetMapping("/users")
    public List<AppUser> appUsers(){
        return accountService.listUsers(); 
    }

    @PostMapping("/users")
    public AppUser addUser(@RequestBody AppUser user){
        return accountService.addNewUser(user);
    }

    @PostMapping("/roles")
    public AppRole addRole(@RequestBody AppRole role){
        return accountService.addNewRole(role);
    }

    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }
}

