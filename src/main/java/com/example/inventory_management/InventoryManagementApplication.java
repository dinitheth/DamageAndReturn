package com.example.inventory_management;

import com.example.inventory_management.Repository.RoleRepository;
import com.example.inventory_management.Repository.UserRepository;
import com.example.inventory_management.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class InventoryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner init(RoleRepository roleRepository,
						   UserRepository userRepository,
						   PasswordEncoder passwordEncoder,
						   UserService userService) {
		return args -> {
			userService.registerAdminUser(passwordEncoder);
		};
	}
}