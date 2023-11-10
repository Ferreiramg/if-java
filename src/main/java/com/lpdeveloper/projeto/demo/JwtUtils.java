package com.lpdeveloper.projeto.demo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lpdeveloper.projeto.demo.models.User;

public class JwtUtils {

    private static final String SECRET_KEY = "your_secret_key";

    public static String generateToken(User usuario) {
        return JWT.create()
                .withSubject(usuario.getUsername())
                .withIssuer("lpdeveloper")
                .withClaim("id", usuario.getId())
                .withExpiresAt(LocalDateTime.now()
                        .plusDays(10)
                        .toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build().verify(token).getSubject();

    }
}
