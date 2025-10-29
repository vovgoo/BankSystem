package com.vovgoo.BankSystem.dto.client.response;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ClientResponse {
    private final UUID id;
    private final String lastName;
    private final String phone;
    private final List<AccountSummaryResponse> accounts;
}