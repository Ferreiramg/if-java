package com.lpdeveloper.projeto.demo.models.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.repositories.UserRepository;

import java.util.Date;

@Component
public class SeedData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public void popularDados() {
        User user1 = new User();
        user1.setName("Admin");
        user1.setEmail("admin@demo.com");
        user1.setCreatedAt(new Date());
        user1.setUpdatedAt(new Date());
        user1.setPassword(passwordEncoder.encode("senha123")); 
        user1.setRole("ROLE_ADMIN");

        User user2 = new User();
        user2.setName("Joe Doe");
        user2.setEmail("joedoe@demo.com");
        user2.setCreatedAt(new Date());
        user2.setUpdatedAt(new Date());
        user2.setPassword(passwordEncoder.encode("senha123")); 
        user2.setRole("ROLE_USER");

        if (userRepository.findByEmail(user1.getEmail()) == null) {
            userRepository.save(user1);
        }

        if (userRepository.findByEmail(user2.getEmail()) == null) {
            userRepository.save(user2);
        }
    }

}
