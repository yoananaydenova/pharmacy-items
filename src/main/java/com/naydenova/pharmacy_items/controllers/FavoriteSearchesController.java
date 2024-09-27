package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.dtos.SearchDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.services.FavoriteSearchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/searches")
public class FavoriteSearchesController {


    private final FavoriteSearchesService favoriteSearchesService;

    @Autowired
    public FavoriteSearchesController(FavoriteSearchesService favoriteSearchesService) {
        this.favoriteSearchesService = favoriteSearchesService;
    }


    @PostMapping
    public ResponseEntity<SearchDto> addFavoriteSearch(@RequestBody SearchDto newFavoriteSearchDto, UsernamePasswordAuthenticationToken token) {
        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();

        final SearchDto savedDto = favoriteSearchesService.saveSearchAsFavorite(newFavoriteSearchDto, username);

        return ResponseEntity.ok(savedDto);
    }
}
