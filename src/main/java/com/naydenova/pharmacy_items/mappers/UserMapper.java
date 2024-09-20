package com.naydenova.pharmacy_items.mappers;

import com.naydenova.pharmacy_items.dtos.SignUpDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
