package com.bachtt.bshop.services;

import com.bachtt.bshop.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);
    Page<User> findAll(Pageable pageable);//page user
    Optional<User> findById(Long id);//detail 1 user by id


}
