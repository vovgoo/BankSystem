package com.vovgoo.BankSystem.repository;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;

public interface StatsRepository {
    StatsResponse getOverview();
}