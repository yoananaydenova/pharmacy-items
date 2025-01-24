package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.CredentialsDto;
import com.naydenova.pharmacy_items.dtos.EditUserDto;
import com.naydenova.pharmacy_items.dtos.SignUpDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.entities.User;
import com.naydenova.pharmacy_items.exceptions.AppException;
import com.naydenova.pharmacy_items.exceptions.UnknownUserException;
import com.naydenova.pharmacy_items.mappers.UserMapper;
import com.naydenova.pharmacy_items.repositories.UserRepository;
import com.naydenova.pharmacy_items.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDto login(CredentialsDto credentialsDto) {
        final User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(UnknownUserException::new);

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        final Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        // TODO Add credential validation
        final User user = userMapper.signUpDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        final User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto editUser(String login, EditUserDto editUserDto) {

        final User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("The user does not exist!", HttpStatus.BAD_REQUEST));

        // TODO Add credential validation
       final String firstName = editUserDto.firstName();
       if(isValidPersonName(firstName)) {
           user.setFirstName(firstName);
       }else {
           throw new AppException("First name is not valid!", HttpStatus.BAD_REQUEST);
       }

        final String lastName = editUserDto.lastName();
        if(isValidPersonName(lastName)) {
            user.setLastName(lastName);
        }else {
            throw new AppException("Last name is not valid!", HttpStatus.BAD_REQUEST);
        }


        final String newLogin = editUserDto.login();
        if(isValidLogin(newLogin)) {
            user.setLogin(newLogin);
        }else {
            throw new AppException("Username is not valid!", HttpStatus.BAD_REQUEST);
        }

        final char [] newPassword = editUserDto.password();
        if(isValidPassword(newPassword)) {
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(newPassword)));
        }

        final User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    // TODO Add password validation
    private boolean isValidPassword(char[] newPassword) {
        return newPassword != null && newPassword.length > 6;
    }

    private boolean isValidLogin(String newLogin) {
        return !newLogin.isBlank();
    }

    private static boolean isValidPersonName(String newData) {
        return !newData.isBlank();
    }

    public UserDto findByLogin(String login) {
        final User user = userRepository.findByLogin(login)
                .orElseThrow(UnknownUserException::new);
        return userMapper.toUserDto(user);
    }
}
