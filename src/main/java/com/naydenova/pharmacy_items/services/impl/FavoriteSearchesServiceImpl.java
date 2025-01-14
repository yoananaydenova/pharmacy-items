package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.SearchDto;
import com.naydenova.pharmacy_items.entities.PharmacyName;
import com.naydenova.pharmacy_items.entities.Search;
import com.naydenova.pharmacy_items.entities.User;
import com.naydenova.pharmacy_items.exceptions.AppException;
import com.naydenova.pharmacy_items.exceptions.UnknownUserException;
import com.naydenova.pharmacy_items.mappers.SearchMapper;
import com.naydenova.pharmacy_items.repositories.SearchRepository;
import com.naydenova.pharmacy_items.repositories.UserRepository;
import com.naydenova.pharmacy_items.services.FavoriteSearchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(UnknownUserException::new);

        final Search savedSearch = saveSearch(newFavoriteSearchDto);

        user.addToSearches(savedSearch);
        userRepository.save(user);
        return searchMapper.toSearchDto(savedSearch);
    }

    @Override
    public List<SearchDto> findAllByUsername(String username) {
        final User user = userRepository.findByLogin(username)
                .orElseThrow(UnknownUserException::new);
        return user.getSearches().stream().map(searchMapper::toSearchDto).collect(Collectors.toList());
    }

    @Override
    public String deleteSearch(Long id, String username) {
        final User user = userRepository.findByLogin(username)
                .orElseThrow(UnknownUserException::new);

        final Search searchToRemove = searchRepository.findById(id)
                .orElseThrow(() -> new AppException("The search isn't present in favorite collection!", HttpStatus.NOT_FOUND));
        searchRepository.deleteById(id);

        return """
                The search %s has been successfully deleted!""".formatted(searchToRemove.getSearchedText());
    }

    private Search saveSearch(SearchDto searchDto) {
        final List<PharmacyName> pharmacies = searchDto.getPharmacies().stream().map(PharmacyName::getPharmacyByName).collect(Collectors.toList());
        final Search search = new Search(searchDto.getSearchedText(), pharmacies, searchDto.getSearchLimit());

        try {
            return searchRepository.save(search);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException("This search is already saved!", HttpStatus.BAD_REQUEST);
        }
    }
}
