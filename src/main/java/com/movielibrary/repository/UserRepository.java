package com.movielibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movielibrary.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
