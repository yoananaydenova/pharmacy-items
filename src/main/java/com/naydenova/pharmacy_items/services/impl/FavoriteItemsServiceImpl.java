package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.entities.Item;
import com.naydenova.pharmacy_items.entities.User;
import com.naydenova.pharmacy_items.exceptions.AppException;
import com.naydenova.pharmacy_items.mappers.ItemMapper;
import com.naydenova.pharmacy_items.repositories.ItemRepository;
import com.naydenova.pharmacy_items.repositories.UserRepository;
import com.naydenova.pharmacy_items.services.FavoriteItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteItemsServiceImpl implements FavoriteItemsService {
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public FavoriteItemsServiceImpl(UserRepository userRepository, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<ItemDto> findAllByUsername(String username) {

        final User user = userRepository.findByLogin(username)
                .orElseThrow(() -> {
                    return new AppException("Unknown user", HttpStatus.NOT_FOUND);
                });
        return user.getFavorites().stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto saveItemAsFavorite(ItemDto newItem, String username) {
        final User user = userRepository.findByLogin(username)
                .orElseThrow(() ->  new AppException("Unknown user", HttpStatus.NOT_FOUND));

         final Optional< Item> result = itemRepository.findByItemUrl(newItem.getItemUrl());
         final Item item = result.orElseGet(() -> createItem(newItem));

        user.addItemToFavorites(item);
        userRepository.save(user);
        return itemMapper.toItemDto(item);
    }

    @Override
    public String deleteItemFromFavorite(Long id, String username) {

        final User user = userRepository.findByLogin(username)
                .orElseThrow(() ->  new AppException("Unknown user", HttpStatus.NOT_FOUND));

        user.removeItemFromFavorites(id);

        userRepository.save(user);

        return """
               Item with id %s has been successfully deleted!""".formatted(id);
    }

    private Item createItem(ItemDto newItem) {
        return itemRepository.save(new Item(newItem.getPharmacyName(),
                newItem.getItemName(), newItem.getPrice(), newItem.getItemUrl(), newItem.getImageUrl()));

    }
}
