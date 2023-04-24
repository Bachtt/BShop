package com.bachtt.bshop.services;

import com.bachtt.bshop.models.Role;
import com.bachtt.bshop.models.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);

}
