package com.bachtt.bshop.services;

import com.bachtt.bshop.models.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);

}
