package com.example.demo;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.Service.AccountService;
import com.example.demo.entites.AppRole;
import com.example.demo.entites.AppUser;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addNewRole(new AppRole(null, "USER", null));
            accountService.addNewRole(new AppRole(null, "ADMIN", null));
            accountService.addNewRole(new AppRole(null, "CUSTOMER_MANAGER", null));
            accountService.addNewRole(new AppRole(null, "PRODUCT_MANAGER", null));
            accountService.addNewRole(new AppRole(null, "BILLS_MANAGER", null));

            accountService.addNewUser(new AppUser(null, "user1", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "user2", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "admin", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "user3", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "user4", "1234", new ArrayList<>()));

            accountService.addRoleToUser("user1", "USER");
            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "ADMIN");
            accountService.addRoleToUser("user2", "USER");
            accountService.addRoleToUser("user2", "CUSTOMER_MANAGER");
            accountService.addRoleToUser("user3", "USER"); 
            accountService.addRoleToUser("user3", "PRODUCT_MANAGER");  
            accountService.addRoleToUser("user4", "USER");
            accountService.addRoleToUser("user4", "BILLS_MANAGER");
        };
    }
}
