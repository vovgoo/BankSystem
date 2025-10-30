package com.vovgoo.BankSystem.dto.account.response;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountSummaryResponse(
        UUID id,
        BigDecimal balance
) {}