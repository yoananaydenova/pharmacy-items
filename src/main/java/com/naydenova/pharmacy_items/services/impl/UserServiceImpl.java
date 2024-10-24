package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.CredentialsDto;
import com.naydenova.pharmacy_items.dtos.SignUpDto;
import com.naydenova.pharmacy_items.dtos.UserDto;
import com.naydenova.pharmacy_items.entities.User;
import com.naydenova.pharmacy_items.exceptions.AppException;
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
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

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

        final User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        final User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto editUser(String login, SignUpDto userDto) {

        final User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("The user does not exist!", HttpStatus.BAD_REQUEST));

        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setLogin(userDto.login());
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        final User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        final User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
}
