package com.bachtt.bshop.controllers;

import com.bachtt.bshop.dto.request.ChangeAvatar;
import com.bachtt.bshop.dto.request.ChangeProfileForm;
import com.bachtt.bshop.dto.request.SignInForm;
import com.bachtt.bshop.dto.request.SignUpForm;
import com.bachtt.bshop.dto.response.JwtResponse;
import com.bachtt.bshop.dto.response.ResponseMessage;
import com.bachtt.bshop.models.Role;
import com.bachtt.bshop.models.RoleName;
import com.bachtt.bshop.models.User;
import com.bachtt.bshop.security.jwt.JwtProvider;
import com.bachtt.bshop.security.jwt.JwtTokenFilter;
import com.bachtt.bshop.security.userpincal.UserPrinciple;
import com.bachtt.bshop.services.impl.RoleServiceImpl;
import com.bachtt.bshop.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userService.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("The username is exists"), HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("The email is exists"), HttpStatus.OK);
        }
        if(signUpForm.getAvatar() == null || signUpForm.getAvatar().trim().isEmpty()){
            signUpForm.setAvatar("https://firebasestorage.googleapis.com/v0/b/monkey-blogging-bcabf.appspot.com/o/images%2Fnhc1601440944.jpg?alt=media&token=263c5763-a922-452c-9cb2-805c6bbcf1c1");
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), signUpForm.getAvatar(),
                passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
//                case "pm":
//                    Role pmRole = roleService.findByName(RoleName.PM)
//                            .orElseThrow(() -> new RuntimeException("Role not found"));
//                    roles.add(pmRole);
//                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);

        return new ResponseEntity<>(new ResponseMessage("Create successfully"), HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAvatar()  , userPrinciple.getAuthorities()));
    }

    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @Valid @RequestBody ChangeAvatar changeAvatar){
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUserNameFromToken(jwt);
        User user;
        try{
            if(changeAvatar.getAvatar() == null){
                return new ResponseEntity<>(new ResponseMessage("no"), HttpStatus.OK);
            }else{
                user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found -> username" + username));
                user.setAvatar(changeAvatar.getAvatar());
                userService.save(user);
            }
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);

        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/change-profile")
    public ResponseEntity<?> changeProfile(HttpServletRequest request, @Valid @RequestBody ChangeProfileForm changeProfileForm){
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUserNameFromToken(jwt);
        User user;
        try {
            if(userService.existsByUsername(changeProfileForm.getUsername())){
                return new ResponseEntity<>(new ResponseMessage("nouser"), HttpStatus.OK);
            }
            if(userService.existsByEmail(changeProfileForm.getEmail())){
                return new ResponseEntity<>(new ResponseMessage("noemail"), HttpStatus.OK);
            }
            user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found with -> useranme"+username));
            user.setName(changeProfileForm.getName());
            user.setUsername(changeProfileForm.getUsername());
            user.setEmail(changeProfileForm.getEmail());
            userService.save(user);
            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()),HttpStatus.NOT_FOUND );
        }
    }
}