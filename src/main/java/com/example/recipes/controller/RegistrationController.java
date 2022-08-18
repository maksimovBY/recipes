package com.example.recipes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.recipes.dto.UserDto;
import com.example.recipes.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto user) {
        if (userService.isEmailAvailable(user)) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
