package com.naydenova.pharmacy_items.services;

import com.naydenova.pharmacy_items.dtos.SearchDto;

public interface FavoriteSearchesService {
    SearchDto saveSearchAsFavorite(SearchDto newFavoriteSearchDto, String username);
}
