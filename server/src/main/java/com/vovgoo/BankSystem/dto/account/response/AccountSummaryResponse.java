package com.vovgoo.BankSystem.dto.account.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AccountSummaryResponse {

    private final UUID id;
    private final BigDecimal balance;
}