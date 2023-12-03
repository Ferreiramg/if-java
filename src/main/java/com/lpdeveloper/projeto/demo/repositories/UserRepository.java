package com.lpdeveloper.projeto.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.lpdeveloper.projeto.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    UserDetails findByEmail(String email);

}