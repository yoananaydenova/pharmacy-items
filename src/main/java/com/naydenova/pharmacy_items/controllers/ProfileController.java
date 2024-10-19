package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public ResponseEntity<UserDto> getUser (UsernamePasswordAuthenticationToken token) {

        final UserDto user = (UserDto)token.getPrincipal();
        return ResponseEntity.ok(user);
    }
}
