package com.lpdeveloper.projeto.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lpdeveloper.projeto.demo.JwtUtils;
import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.dto.Login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Autenticação")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Autenticação de usuário")
    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso", content = @Content(mediaType = "application/text", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @PostMapping("/login")
    public String login(@Parameter(description = "Credenciais de login", required = true) @RequestBody Login login) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.login,
                login.password);

        Authentication autheticated = this.authenticationManager.authenticate(authToken);

        var userAuth = (User) autheticated.getPrincipal();

        return JwtUtils.generateToken(userAuth);

    }

    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }

}
