package com.vovgoo.BankSystem.dto.account.response;

import com.vovgoo.BankSystem.dto.client.response.ClientSummaryResponse;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AccountResponse {

    private final UUID id;

    private final BigDecimal balance;

    private final ClientSummaryResponse client;
}