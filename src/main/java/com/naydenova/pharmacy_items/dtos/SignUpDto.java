package com.naydenova.pharmacy_items.dtos;

import jakarta.validation.constraints.NotEmpty;

public record SignUpDto(@NotEmpty
                        String firstName,

                        @NotEmpty
                        String lastName,

                        @NotEmpty
                        String login,

                        @NotEmpty
                        char[] password) {
}
