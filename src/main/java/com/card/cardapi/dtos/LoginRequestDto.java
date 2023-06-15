package com.card.cardapi.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDto {
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Password is required")
    private String password;
}
