package com.bachtt.bshop.controllers;

import com.bachtt.bshop.models.User;
import com.bachtt.bshop.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

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
}
