package com.vovgoo.BankSystem.dto.stats;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class StatsResponse {

    private final long totalClients;
    private final long clientsWithAccounts;
    private final long clientsWithoutAccounts;
    private final long totalAccounts;

    private final BigDecimal totalBalance;
    private final BigDecimal averageBalance;
    private final BigDecimal maxBalance;
    private final BigDecimal minBalance;

    private final long accountsOpenedLastMonth;
    private final long accountsClosedLastMonth;

    private final Map<LocalDate, Long> newClientsPerDay;
    private final Map<LocalDate, Long> newAccountsPerDay;
}