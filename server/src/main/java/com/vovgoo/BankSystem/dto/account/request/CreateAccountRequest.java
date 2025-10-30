package com.vovgoo.BankSystem.dto.account.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountRequest(

        @NotNull(message = "ID клиента обязателен")
        UUID clientId

) {}