package com.vovgoo.BankSystem.dto.client.request;

import jakarta.validation.constraints.NotNull;

public record SearchClientRequest(

        @NotNull(message = "Фамилия не должна быть null")
        String lastName
) {}