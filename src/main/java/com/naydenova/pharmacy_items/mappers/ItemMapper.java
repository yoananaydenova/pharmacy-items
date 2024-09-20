package com.naydenova.pharmacy_items.mappers;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.entities.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);
}
