package com.vovgoo.BankSystem.dto.stats;

import java.math.BigDecimal;

public record StatsResponse(
        long totalClients,
        long clientsWithAccounts,
        long clientsWithoutAccounts,
        long totalAccounts,
        BigDecimal totalBalance,
        BigDecimal averageBalance,
        BigDecimal maxBalance,
        BigDecimal minBalance
) {}