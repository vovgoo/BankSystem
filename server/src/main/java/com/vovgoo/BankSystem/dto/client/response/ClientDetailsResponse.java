package com.vovgoo.BankSystem.dto.client.response;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import com.vovgoo.BankSystem.dto.common.PageResponse;

import java.util.List;
import java.util.UUID;

public record ClientDetailsResponse(
        UUID id,
        String lastName,
        String phone,
        PageResponse<AccountSummaryResponse> accounts
) {}