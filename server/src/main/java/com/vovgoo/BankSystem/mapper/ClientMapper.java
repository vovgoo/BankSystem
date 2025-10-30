package com.vovgoo.BankSystem.mapper;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.entity.Client;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class ClientMapper {

    public ClientDetailsResponse toClientDetailsResponse(Client client) {
        if (client == null) return null;

        List<AccountSummaryResponse> accounts = Collections.emptyList();

        if (client.getAccounts() != null && !client.getAccounts().isEmpty()) {
            accounts = client.getAccounts().stream()
                    .map(account -> new AccountSummaryResponse(account.getId(), account.getBalance()))
                    .toList();
        }

        return new ClientDetailsResponse(
                client.getId(),
                client.getLastName(),
                client.getPhone(),
                accounts
        );
    }

    public ClientResponse toClientResponse(Client client, BigDecimal totalBalance) {
        if (client == null) return null;

        return new ClientResponse(
                client.getId(),
                client.getLastName(),
                client.getPhone(),
                totalBalance
        );
    }
}