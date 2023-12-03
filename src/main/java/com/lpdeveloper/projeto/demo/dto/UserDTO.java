package com.lpdeveloper.projeto.demo.dto;

public class UserDTO {
    public String name;
    public String email;
    public Long id;
    public String role;

    public UserDTO(String name, String email, Long id, String role) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.role = role;
    }

}