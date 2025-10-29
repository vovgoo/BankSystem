package com.vovgoo.BankSystem.mapper;

import com.vovgoo.BankSystem.dto.account.response.AccountSummaryResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientDetailsResponse;
import com.vovgoo.BankSystem.dto.client.response.ClientResponse;
import com.vovgoo.BankSystem.entity.Client;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    public ClientDetailsResponse to(Client client) {
        if (client == null) return null;

        return ClientDetailsResponse.builder()
                .id(client.getId())
                .lastName(client.getLastName())
                .phone(client.getPhone())
                .accounts(client.getAccounts()
                        .stream()
                        .map(account -> AccountSummaryResponse.builder()
                                .id(account.getId())
                                .balance(account.getBalance())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public ClientResponse to(Client client, BigDecimal totalBalance) {
        if (client == null) return null;

        return ClientResponse.builder()
                .id(client.getId())
                .lastName(client.getLastName())
                .phone(client.getPhone())
                .totalBalance(totalBalance)
                .build();
    }
}