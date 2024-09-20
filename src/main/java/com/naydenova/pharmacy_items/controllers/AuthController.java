package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.config.UserAuthenticationProvider;
import com.naydenova.pharmacy_items.dtos.CredentialsDto;
import com.naydenova.pharmacy_items.dtos.SignUpDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.net.URI;


@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    public AuthController(UserService userService, UserAuthenticationProvider userAuthenticationProvider) {
        this.userService = userService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        final UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
        final UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(user.login()));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

}

