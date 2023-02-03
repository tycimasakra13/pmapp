package com.project.bootstrap;

import com.project.model.User;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserLoader implements CommandLineRunner {
    public final UserRepository userRepository;

    public UserLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        loadUsers();
    }
    
    private void loadUsers() {
        if (userRepository.count() == 0) {
            insertNewUser("USER");
            insertNewUser("ADMIN");
        }
    }
    
    private void insertNewUser(String role) {
        User user = new User();
    String encodedPassword = passwordEncoder.encode(role.toLowerCase());

    
    
        user.setUsername(role.toLowerCase());
        user.setPassword(encodedPassword);
        user.setRole(role);
  
        userRepository.save(user);
        System.out.println("Sample User Loaded " + role);   
    }
}


