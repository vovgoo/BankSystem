package com.vovgoo.BankSystem.dto.account.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateAccountRequest {

    @NotNull(message = "ID клиента обязателен")
    private final UUID clientId;
}