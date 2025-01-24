package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.config.UserAuthenticationProvider;
import com.naydenova.pharmacy_items.dtos.EditUserDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Validated
public class ProfileController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public ProfileController(UserService userService, UserAuthenticationProvider userAuthenticationProvider) {
        this.userService = userService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser (UsernamePasswordAuthenticationToken token) {

        final UserDto user = (UserDto)token.getPrincipal();
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody  EditUserDto userData, UsernamePasswordAuthenticationToken token) {
        final UserDto user = (UserDto)token.getPrincipal();
        final UserDto userDto = userService.editUser(user.getLogin(), userData);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin()));
        return ResponseEntity.ok(userDto);
    }
}
