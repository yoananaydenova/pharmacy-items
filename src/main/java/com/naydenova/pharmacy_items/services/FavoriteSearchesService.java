package com.naydenova.pharmacy_items.services;

import com.naydenova.pharmacy_items.dtos.SearchDto;

import java.util.List;

public interface FavoriteSearchesService {
    SearchDto saveSearchAsFavorite(SearchDto newFavoriteSearchDto, String username);

    List<SearchDto> findAllByUsername(String username);

    String deleteSearch(Long id, String username);
}
