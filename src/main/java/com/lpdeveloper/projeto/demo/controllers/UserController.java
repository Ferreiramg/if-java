package com.lpdeveloper.projeto.demo.controllers;

import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint para obter todos os usuários
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Endpoint para obter um usuário por ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Endpoint para criar um novo usuário
    @PostMapping
    public User createUser(@RequestBody User user) {

        String plainPassword = new BCryptPasswordEncoder().encode(user.getPassword());

        user.setPassword(plainPassword);
        
        return userRepository.save(user);
    }

    // Endpoint para atualizar um usuário existente
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setName(updatedUser.getName());
            user.setPassword(updatedUser.getPassword());
            user.setEmail(updatedUser.getEmail());
            user.setCreatedAt(updatedUser.getCreatedAt());
            user.setUpdatedAt(updatedUser.getUpdatedAt());
            return userRepository.save(user);
        }

        return null;
    }

    // Endpoint para deletar um usuário
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
