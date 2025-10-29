package com.vovgoo.BankSystem.dto.client.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ClientSummaryResponse {

    private final UUID id;

    private final String lastName;

    private final String phone;
}