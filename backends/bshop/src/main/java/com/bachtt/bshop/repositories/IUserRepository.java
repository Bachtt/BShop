package com.bachtt.bshop.repositories;

import com.bachtt.bshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);//tim kiem username co ton tai trong db khong
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
