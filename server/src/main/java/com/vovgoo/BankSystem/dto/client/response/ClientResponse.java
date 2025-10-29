package com.vovgoo.BankSystem.dto.client.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ClientResponse {

    private final UUID id;

    private final String lastName;

    private final String phone;

    private final BigDecimal totalBalance;
}