package com.vovgoo.BankSystem.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record StatsResponse(
        long totalClients,
        long clientsWithAccounts,
        long clientsWithoutAccounts,
        long totalAccounts,
        BigDecimal totalBalance,
        BigDecimal averageBalance,
        BigDecimal maxBalance,
        BigDecimal minBalance,
        long accountsOpenedLastMonth,
        long accountsClosedLastMonth,
        Map<LocalDate, Long> newClientsPerDay,
        Map<LocalDate, Long> newAccountsPerDay
) {}