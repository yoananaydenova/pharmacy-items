package com.naydenova.pharmacy_items.dtos;

import jakarta.validation.constraints.Size;

public record EditUserDto(
        @Size(min = 2, max = 20, message
                = "First name must be between 1 and 20 characters")
        String firstName,

        @Size(min = 2, max = 20, message
                = "Last name must be between 1 and 20 characters")
        String lastName,

        @Size(min = 2, max = 10, message
                = "Login name must be between 4 and 10 characters")
        String login,

        @Size(min = 2, max = 15, message
                = "Password must be between 6 and 15 characters")
        char[] password) {
}