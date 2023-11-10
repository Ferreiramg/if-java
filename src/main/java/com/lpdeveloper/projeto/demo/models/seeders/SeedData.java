package com.lpdeveloper.projeto.demo.models.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.repositories.UserRepository;

import java.util.Date;
import java.util.Optional;

@Component
public class SeedData {

    @Autowired
    private UserRepository userRepository; // Supondo que você tenha um repositório para User

    @Autowired
    private PasswordEncoder passwordEncoder; // Você vai precisar de um encoder de senha

    public void popularDados() {
        User user1 = new User();
        user1.setName("Admin");
        user1.setEmail("admin@demo.com");
        user1.setCreatedAt(new Date());
        user1.setUpdatedAt(new Date());
        user1.setPassword(passwordEncoder.encode("senha123")); // Codifique a senha

        User user2 = new User();
        user2.setName("Joe Doe");
        user2.setEmail("joedoe@demo.com");
        user2.setCreatedAt(new Date());
        user2.setUpdatedAt(new Date());
        user2.setPassword(passwordEncoder.encode("senha123")); // Codifique a senha

        if (userRepository.findByEmail(user1.getEmail()) == null) {
            userRepository.save(user1);
        }

        if (userRepository.findByEmail(user2.getEmail()) == null) {
            userRepository.save(user2);
        }
    }

}
