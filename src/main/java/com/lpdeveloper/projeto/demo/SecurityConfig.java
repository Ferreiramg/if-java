package com.lpdeveloper.projeto.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lpdeveloper.projeto.demo.middlewares.CorsHeadersFilter;
import com.lpdeveloper.projeto.demo.middlewares.JWTFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    JWTFilter jwtFilter;

    @Autowired
    CorsHeadersFilter corsHeadersFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/login")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/", "/static/**", "/favicon.ico", "/manifest.json")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(corsHeadersFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, CorsHeadersFilter.class)
                // .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
