package com.naydenova.pharmacy_items.services;

import com.naydenova.pharmacy_items.dtos.ItemDto;

import java.util.List;

public interface FavoriteItemsService {
    List<ItemDto> findAllByUsername(String token);

    ItemDto saveItemAsFavorite(ItemDto newItem, String username);

    String deleteItemFromFavorite(Long id, String username);
}
