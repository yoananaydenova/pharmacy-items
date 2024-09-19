package com.naydenova.pharmacy_items.services;

import com.naydenova.pharmacy_items.dtos.CredentialsDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.dtos.SignUpDto;

public interface UserService {

    UserDto login(CredentialsDto credentialsDto);

    UserDto register(SignUpDto userDto);

    UserDto findByLogin(String login);


}
