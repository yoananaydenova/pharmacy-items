package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.SearchDto;
import com.naydenova.pharmacy_items.entities.Search;
import com.naydenova.pharmacy_items.entities.User;
import com.naydenova.pharmacy_items.exceptions.AppException;
import com.naydenova.pharmacy_items.mappers.SearchMapper;
import com.naydenova.pharmacy_items.repositories.SearchRepository;
import com.naydenova.pharmacy_items.repositories.UserRepository;
import com.naydenova.pharmacy_items.services.FavoriteSearchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FavoriteSearchesServiceImpl implements FavoriteSearchesService {

    private final UserRepository userRepository;
    private final SearchRepository searchRepository;

    private final SearchMapper searchMapper;

    @Autowired
    public FavoriteSearchesServiceImpl(UserRepository userRepository, SearchRepository searchRepository, SearchMapper searchMapper) {
        this.userRepository = userRepository;
        this.searchRepository = searchRepository;
        this.searchMapper = searchMapper;
    }

    @Override
    public SearchDto saveSearchAsFavorite(SearchDto newFavoriteSearchDto, String username) {
        final User user = userRepository.findByLogin(username)
                .orElseThrow(() ->  new AppException("Unknown user", HttpStatus.NOT_FOUND));

        final Search savedSearch = saveSearch(newFavoriteSearchDto);

        user.addToSearches(savedSearch);
        userRepository.save(user);
        return searchMapper.toSearchDto(savedSearch);
    }

    private Search saveSearch(SearchDto searchDto) {
        return searchRepository.save(searchMapper.toSearch(searchDto));
    }
}