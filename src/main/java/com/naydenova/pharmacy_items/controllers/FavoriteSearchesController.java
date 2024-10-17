package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.dtos.SearchDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.services.FavoriteSearchesService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/searches")
public class FavoriteSearchesController {


    private final FavoriteSearchesService favoriteSearchesService;

    @Autowired
    public FavoriteSearchesController(FavoriteSearchesService favoriteSearchesService) {
        this.favoriteSearchesService = favoriteSearchesService;
    }

    @GetMapping
    public ResponseEntity<List<SearchDto>> findAllByUser(UsernamePasswordAuthenticationToken token) {

        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();
        final List<SearchDto> items= favoriteSearchesService.findAllByUsername(username);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<SearchDto> addSearch(@RequestBody SearchDto newFavoriteSearchDto, UsernamePasswordAuthenticationToken token) {
        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();

        final SearchDto savedDto = favoriteSearchesService.saveSearchAsFavorite(newFavoriteSearchDto, username);

        return ResponseEntity.ok(savedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSearch(@PathVariable @Min(1) Long id, UsernamePasswordAuthenticationToken token) {

        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();

        final String message = favoriteSearchesService.deleteSearch(id, username);

        return ResponseEntity.ok(message);
    }
}
