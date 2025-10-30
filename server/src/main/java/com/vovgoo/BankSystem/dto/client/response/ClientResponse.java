package com.vovgoo.BankSystem.dto.client.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String lastName,
        String phone,
        BigDecimal totalBalance
) {}