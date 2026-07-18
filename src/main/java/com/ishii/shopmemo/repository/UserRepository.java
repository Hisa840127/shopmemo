package com.ishii.shopmemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishii.shopmemo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}