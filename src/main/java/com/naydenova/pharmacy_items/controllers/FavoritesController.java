package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.services.FavoriteItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoriteItemsService favoriteItemsService;

    @Autowired
    public FavoritesController(FavoriteItemsService favoriteItemsService) {
        this.favoriteItemsService = favoriteItemsService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll(UsernamePasswordAuthenticationToken token) {

        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();
        final List<ItemDto> items= favoriteItemsService.findAllByUsername(username);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<ItemDto> addFavorite(@RequestBody ItemDto newItem, UsernamePasswordAuthenticationToken token) {
        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();

        final ItemDto savedItemDTO = favoriteItemsService.saveItemAsFavorite(newItem, username);

        return ResponseEntity.ok(savedItemDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Long id, UsernamePasswordAuthenticationToken token) {

        final UserDto user = (UserDto)token.getPrincipal();
        final String username =  user.getLogin();

        final String message = favoriteItemsService.deleteItemFromFavorite(id, username);

        return ResponseEntity.ok(message);
    }

}
