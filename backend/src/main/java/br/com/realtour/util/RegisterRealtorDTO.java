package br.com.realtour.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRealtorDTO(
        @NotBlank
        @Email
        String email,
        @NotEmpty
        @Size(min = 4, max = 15) String username,
        @NotEmpty
        @Size(min = 4,max = 15)
        String password,
        @NotEmpty
        @Size(min = 8, max = 9)
        String creci) {
}
