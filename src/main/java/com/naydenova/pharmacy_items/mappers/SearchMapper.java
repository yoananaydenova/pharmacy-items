package com.naydenova.pharmacy_items.mappers;

import com.naydenova.pharmacy_items.dtos.SearchDto;
import com.naydenova.pharmacy_items.entities.Search;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    SearchDto toSearchDto(Search search);
    Search toSearch(SearchDto search);
}
