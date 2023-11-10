package com.lpdeveloper.projeto.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lpdeveloper.projeto.demo.JwtUtils;
import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.dto.Login;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody Login login) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.login,
                login.password);

        Authentication autheticated = this.authenticationManager.authenticate(authToken);

        var userAuth = (User) autheticated.getPrincipal();

        return JwtUtils.generateToken(userAuth);

    }

}
