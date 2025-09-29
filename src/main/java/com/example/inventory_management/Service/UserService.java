package com.example.inventory_management.Service;

import com.example.inventory_management.Models.Role;
import com.example.inventory_management.Models.User;
import com.example.inventory_management.Repository.RoleRepository;
import com.example.inventory_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Remove PasswordEncoder autowiring from here
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()))
        );
    }

    public String getUserNameById(Long id) {
        if (id == null) {
            return "Unknown User";
        }
        return userRepository.findById(id.intValue())
                .map(User::getUserName)
                .orElse("Unknown User");
    }

    // Update this method to accept PasswordEncoder as parameter
    public void registerAdminUser(PasswordEncoder passwordEncoder) {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByRoleName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setRoleName("ADMIN");
                adminRole.setDescription("Administrator with full access");
                adminRole = roleRepository.saveAndFlush(adminRole); // Save and flush immediately
            }

            User admin = new User();
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            admin.setActive(true);
            userRepository.save(admin);
        }
    }
}