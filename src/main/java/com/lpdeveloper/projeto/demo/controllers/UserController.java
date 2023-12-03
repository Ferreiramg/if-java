package com.lpdeveloper.projeto.demo.controllers;

import com.lpdeveloper.projeto.demo.dto.UserDTO;
import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Obter todos os usuários (Somente administradores)", description = "Este endpoint retorna todos os usuários cadastrados.")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Obter um usuário por ID (Somente administradores)", description = "Este endpoint retorna um usuário com base no ID fornecido. ")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User getUserById(@PathVariable @Parameter(description = "ID do usuário") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Operation(summary = "Criar um novo usuário (Somente administradores)", description = "Este endpoint cria um novo usuário com senha padrão (senha123)")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public User createUser(@RequestBody @Parameter(description = "Dados do usuário a serem criados") UserDTO user) {

        String plainPassword = new BCryptPasswordEncoder().encode("senha123");
        User newUser = new User();

        newUser.setName(user.name);
        newUser.setEmail(user.email);
        newUser.setRole(user.role);
        newUser.setPassword(plainPassword);

        return userRepository.save(newUser);
    }

    @Operation(summary = "Atualizar um usuário existente (Somente administradores)", description = "Este endpoint atualiza um usuário existente com base no ID fornecido.")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUser(@PathVariable @Parameter(description = "ID do usuário a ser atualizado") Long id,
            @RequestBody @Parameter(description = "Dados do usuário atualizado") UserDTO updatedUser) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setName(updatedUser.name);
            user.setEmail(updatedUser.email);
            user.setUpdatedAt(new Date());
            return userRepository.save(user);
        }

        return null;
    }

    @Operation(summary = "Deletar um usuário (Somente administradores)", description = "Este endpoint exclui um usuário com base no ID fornecido.")
     @ApiResponse(responseCode = "403", description = "Acesso negado")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable @Parameter(description = "ID do usuário a ser excluído") Long id) {
        userRepository.deleteById(id);
    }

    @Operation(summary = "Atualizar a senha do usuário autenticado", description = "Este endpoint atualiza a senha do usuário autenticado.")
    @PutMapping("/password")
    public User updatePassword(@RequestBody @Parameter(description = "Nova senha") String passString) {

        var authUser = AuthController.getAuthenticatedUser();
        String plainPassword = new BCryptPasswordEncoder().encode(passString);

        authUser.setPassword(plainPassword);

        return userRepository.save(authUser);
    }

}
