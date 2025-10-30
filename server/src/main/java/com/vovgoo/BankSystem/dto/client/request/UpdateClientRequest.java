package com.vovgoo.BankSystem.dto.client.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateClientRequest(

        @NotNull(message = "ID клиента обязателен")
        UUID id,

        @NotBlank(message = "Фамилия не должна быть пустой")
        @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
        @Pattern(
                regexp = "^[A-Za-zА-Яа-яЁё]+(-[A-Za-zА-Яа-яЁё]+)?$",
                message = "Фамилия должна быть одним словом или двойная через тире"
        )
        String lastName,

        @NotBlank(message = "Телефон обязателен")
        @Pattern(
                regexp = "^\\+375\\d{9}$",
                message = "Телефон должен быть в формате +375XXXXXXXXX"
        )
        String phone

) {}