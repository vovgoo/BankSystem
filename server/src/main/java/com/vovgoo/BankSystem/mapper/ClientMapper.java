package com.vovgoo.BankSystem.mapper;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.dto.common.PageResponse;
import com.vovgoo.BankSystem.entity.Account;
import com.vovgoo.BankSystem.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClientMapper {

    public ClientDetailsResponse toClientDetailsResponse(Client client, Page<Account> accounts) {
        if (client == null) return null;

        Page<Account> safeAccounts = (accounts != null) ? accounts : Page.empty();

        Page<AccountSummaryResponse> accountSummaryResponses = safeAccounts
                .map(account -> new AccountSummaryResponse(account.getId(), account.getBalance()));

        return new ClientDetailsResponse(
                client.getId(),
                client.getLastName(),
                client.getPhone(),
                PageResponse.of(accountSummaryResponses)
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