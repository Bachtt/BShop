package com.bachtt.bshop.controllers;

import com.bachtt.bshop.dto.request.ChangeRoleForm;
import com.bachtt.bshop.dto.response.ResponseMessage;
import com.bachtt.bshop.models.Role;
import com.bachtt.bshop.models.RoleName;
import com.bachtt.bshop.models.User;
import com.bachtt.bshop.services.impl.RoleServiceImpl;
import com.bachtt.bshop.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @GetMapping
    public ResponseEntity<?> pageUser(@PageableDefault(sort = "username", direction = Sort.Direction.ASC)Pageable pageable){
        Page<User> userPage = userService.findAll(pageable);
        if(userPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailUser(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/change/role/{id}")
    public ResponseEntity<?> changeRoleUser(@PathVariable Long id,@RequestBody ChangeRoleForm changeRoleForm){
        Optional<User> user = userService.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String strRoles = changeRoleForm.getRole();
        Set<Role> roles = new HashSet<>();
        switch (strRoles){
            case "admin":
                Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(()-> new RuntimeException("Role not found"));
                roles.add(adminRole);
                break;
            case "user":
                Role userRole = roleService.findByName(RoleName.USER).orElseThrow(()-> new RuntimeException("Role not found"));
                roles.add(userRole);
                break;
        }
        user.get().setRoles(roles);
        userService.save(user.get());
        return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
    }
}
