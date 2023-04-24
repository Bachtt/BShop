package com.bachtt.bshop.services.impl;

import com.bachtt.bshop.models.Role;
import com.bachtt.bshop.models.RoleName;
import com.bachtt.bshop.repositories.IRoleRepository;
import com.bachtt.bshop.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
