package com.vovgoo.BankSystem.dto.client.response;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;

import java.util.List;
import java.util.UUID;

public record ClientDetailsResponse(
        UUID id,
        String lastName,
        String phone,
        List<AccountSummaryResponse> accounts
) {}