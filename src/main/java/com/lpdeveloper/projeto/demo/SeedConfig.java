package com.lpdeveloper.projeto.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.lpdeveloper.projeto.demo.models.seeders.SeedData;

import javax.annotation.PostConstruct;

@Configuration
public class SeedConfig {

    @Autowired
    private SeedData seedData;

    @PostConstruct
    public void popularBancoDeDados() {
        seedData.popularDados();
    }
}
